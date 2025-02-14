package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.presenter.UserPresenter;
import com.TheLa.services.SendMail;
import com.TheLa.utils.Constant;
import com.example.TheLa.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userPresenter = new ViewModelProvider(this).get(UserPresenter.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addEvents();
    }

    private void addEvents() {
        binding.btnRegister.setOnClickListener(
                v -> btnRegisterClick()
        );
    }

    private void btnRegisterClick() {
        String name = binding.edName.getText().toString().trim();
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPass.getText().toString().trim();
        String rePassword = binding.edRePass.getText().toString().trim();

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
        String message = "Hi " + name + ",\n" +
                "Use the code below to confirm your account:\n\n" +
                code + "\n\n" +
                "Thanks for joining us!";

        if (SendMail.sendEmail(email, subject, message)) {
            if (userPresenter.addUser(user)) {
                Intent intent = new Intent(RegisterActivity.this, VerificationAccountActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("feature", "Register");
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "An error occurred while sending the email. Please check your email address and try again!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("RegisterActivity", "addUser returned false");

        }

    }

    private boolean isValidInput(String name, String email, String password, String rePassword) {
        boolean isValid = true;
        if (name.isEmpty()) {
            binding.edName.setError("Please enter your name!");
            binding.edName.requestFocus();
            isValid = false;
        }

        if (email.isEmpty()) {
            binding.edEmail.setError("Please enter your email!");
            binding.edEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Invalid email!");
            binding.edEmail.requestFocus();
            isValid = false;
        } else if (userPresenter.getUserFindByEmail(email) != null) {
            binding.edEmail.setError("This email is already linked to another account!");
            binding.edEmail.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.edPass.setError("Please enter your password!");
            binding.edPass.requestFocus();
            isValid = false;
        } else if (!isPasswordStrong(password)) {
            isValid = false;
        }

        if (rePassword.isEmpty()) {
            binding.edRePass.setError("Please confirm your password!");
            binding.edRePass.requestFocus();
            isValid = false;
        } else if (!password.equals(rePassword)) {
            binding.edRePass.setError("Passwords do not match!");
            binding.edRePass.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
        if (password.length() < 8) {
            binding.edPass.setError("Password must be at least 8 characters long!");
            binding.edPass.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái hoa
        if (!password.matches(".*[A-Z].*")) {
            binding.edPass.setError("Password must contain at least one uppercase letter!");
            binding.edPass.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái thường
        if (!password.matches(".*[a-z].*")) {
            binding.edPass.setError("Password must contain at least one lowercase letter!");
            binding.edPass.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một số
        if (!password.matches(".*[0-9].*")) {
            binding.edPass.setError("Password must contain at least one number!");
            binding.edPass.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            binding.edPass.setError("Password must contain at least one special character!");
            binding.edPass.requestFocus();
            return false;
        }

        return true;  // Mật khẩu đủ mạnh
    }

}