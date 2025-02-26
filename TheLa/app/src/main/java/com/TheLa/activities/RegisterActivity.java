package com.TheLa.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.services.implement.UserService;
import com.TheLa.configs.SendMail;
import com.TheLa.utils.Constant;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    UserService userService;
    private boolean isPasswordVisible = false;
    private boolean isRePasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        PasswordClick();

        addEvents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void PasswordClick() {
        binding.etPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Get the drawable at the end of the EditText
                if (binding.etPassword.getCompoundDrawablesRelative()[2] != null &&
                        event.getRawX() >= (binding.etPassword.getRight() -
                                binding.etPassword.getCompoundDrawablesRelative()[2].getBounds().width())) {
                    // Toggle password visibility
                    if (isPasswordVisible) {
                        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
                    } else {
                        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
                    }
                    // Move cursor to the end
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isPasswordVisible = !isPasswordVisible;
                    return true;
                }
            }
            return false;
        });

        binding.etRepeatPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Get the drawable at the end of the EditText
                if (binding.etRepeatPassword.getCompoundDrawablesRelative()[2] != null &&
                        event.getRawX() >= (binding.etRepeatPassword.getRight() -
                                binding.etRepeatPassword.getCompoundDrawablesRelative()[2].getBounds().width())) {
                    // Toggle password visibility
                    if (isRePasswordVisible) {
                        binding.etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.etRepeatPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
                    } else {
                        binding.etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.etRepeatPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
                    }
                    // Move cursor to the end
                    binding.etRepeatPassword.setSelection(binding.etRepeatPassword.getText().length());
                    isRePasswordVisible = !isRePasswordVisible;
                    return true;
                }
            }
            return false;
        });
    }

    private void addEvents() {
        binding.btnRegister.setOnClickListener(
                v -> btnRegisterClick()
        );

        binding.tvLogIn.setOnClickListener(
                v -> btnLoginClick()
        );
    }

    private void btnLoginClick() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void btnRegisterClick() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String rePassword = binding.etRepeatPassword.getText().toString().trim();

        // Validate input
        if (!isValidInput(name, email, password, rePassword)) {
            return;
        }

        String code = SendMail.getRandom();

        User user = new User(
                name,
                email,
                password,
                code,
                null,
                null,
                Constant.ROLE_CUSTOMER,
                false
        );

        String subject = "Confirm Your Account";
        String message = "Hi " + user.getName() + ",\n" +
                "Use the code below to confirm your account:\n\n" +
                code + "\n\n" +
                "Thanks for joining us!";

        if (SendMail.sendEmail(email, subject, message)) {
            User newUser = userService.addUser(user);
            if (newUser != null) {
                Intent intent = new Intent(RegisterActivity.this, VerificationAccountActivity.class);
                intent.putExtra("user", newUser);
                intent.putExtra("feature", "Register");
                startActivity(intent);
            }
        } else {
            Toast.makeText(RegisterActivity.this, "An error occurred while sending the email. Please check your email address and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput(String name, String email, String password, String rePassword) {
        boolean isValid = true;
        if (name.isEmpty()) {
            binding.etName.setError("Please enter your name!");
            binding.etName.requestFocus();
            isValid = false;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError("Please enter your email!");
            binding.etEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Invalid email!");
            binding.etEmail.requestFocus();
            isValid = false;
        } else if (userService.getUserFindByEmail(email) != null) {
            binding.etEmail.setError("This email is already linked to another account!");
            binding.etEmail.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Please enter your password!");
            binding.etPassword.requestFocus();
            isValid = false;
        } else if (!isPasswordStrong(password)) {
            isValid = false;
        }

        if (rePassword.isEmpty()) {
            binding.etRepeatPassword.setError("Please confirm your password!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        } else if (!password.equals(rePassword)) {
            binding.etRepeatPassword.setError("Passwords do not match!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
        if (password.length() < 8) {
            binding.etPassword.setError("Password must be at least 8 characters long!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái hoa
        if (!password.matches(".*[A-Z].*")) {
            binding.etPassword.setError("Password must contain at least one uppercase letter!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái thường
        if (!password.matches(".*[a-z].*")) {
            binding.etPassword.setError("Password must contain at least one lowercase letter!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một số
        if (!password.matches(".*[0-9].*")) {
            binding.etPassword.setError("Password must contain at least one number!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            binding.etPassword.setError("Password must contain at least one special character!");
            binding.etPassword.requestFocus();
            return false;
        }

        return true;  // Mật khẩu đủ mạnh
    }

}