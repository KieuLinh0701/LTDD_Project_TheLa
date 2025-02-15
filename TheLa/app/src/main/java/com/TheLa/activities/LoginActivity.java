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
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.services.implement.UserService;
import com.TheLa.configs.SendMail;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    UserService userService;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Kiểm tra nếu đã đăng nhập rồi, chuyển sang trang Home
        // SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        // if (sharedPreferenceManager.getStringValue(SharedPreferenceManager.AUTH_TOKEN) != null) {
        //    switchToHomeActivity();
        // }
        PasswordClick();
        addEvents();
    }

    private void addEvents() {
        binding.btnLogin.setOnClickListener(v -> btnLoginClick());
        binding.tvForgotPassword.setOnClickListener(v -> tvForgotPasswordClick());
        binding.tvSignUp.setOnClickListener(v -> tvSignUpClick());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void PasswordClick() {
        binding.edPass.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Get the drawable at the end of the EditText
                if (binding.edPass.getCompoundDrawablesRelative()[2] != null &&
                        event.getRawX() >= (binding.edPass.getRight() -
                                binding.edPass.getCompoundDrawablesRelative()[2].getBounds().width())) {
                    // Toggle password visibility
                    if (isPasswordVisible) {
                        binding.edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.edPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
                    } else {
                        binding.edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.edPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
                    }
                    // Move cursor to the end
                    binding.edPass.setSelection(binding.edPass.getText().length());
                    isPasswordVisible = !isPasswordVisible;
                    return true;
                }
            }
            return false;
        });
    }

    private void tvSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void tvForgotPasswordClick() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordEmailActivity.class);
        startActivity(intent);
    }

    private void btnLoginClick() {
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPass.getText().toString().trim();

        // Validate input
        if (!isValidInput(email, password)) {
            return;
        }

        User user = userService.getUserFindByEmail(email);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            if (user.getActive()) {
                SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(LoginActivity.this);
                sharedPreferenceManager.setStringValue(SharedPreferenceManager.AUTH_TOKEN, "mock_auth_token");
                switchToHomeActivity();
            } else {
                String code = SendMail.getRandom();
                user.setCode(code);
                String subject = "Confirm Your Account";
                String message = "Hi " + user.getName() + ",\n" +
                        "Use the code below to confirm your account:\n\n" +
                        code + "\n\n" +
                        "Thanks for joining us!";

                if (SendMail.sendEmail(email, subject, message)) {
                    Intent intent = new Intent(LoginActivity.this, VerificationAccountActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("feature", "Login");
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to send the verification email. Please try again later!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(LoginActivity.this, "Login failed, please check your email or password!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput(String email, String password) {
        boolean isValid = true;
        if (email.isEmpty()) {
            binding.edEmail.setError("Please enter your email!");
            binding.edEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Invalid email!");
            binding.edEmail.requestFocus();
            isValid = false;
        } else if (password.isEmpty()) {
            binding.edPass.setError("Please enter your password!");
            binding.edPass.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void switchToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}