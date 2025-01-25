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
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userPresenter = new ViewModelProvider(this).get(UserPresenter.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Kiểm tra nếu đã đăng nhập rồi, chuyển sang trang Home
        // SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        // if (sharedPreferenceManager.getStringValue(SharedPreferenceManager.AUTH_TOKEN) != null) {
        //    switchToHomeActivity();
        // }

        addEvents();
    }

    private void addEvents() {
        binding.btnLogin.setOnClickListener(v -> btnLoginClick());
        binding.tvForgotPassword.setOnClickListener(v -> tvForgotPasswordClick());
        binding.tvSignUp.setOnClickListener(v -> tvSignUpClick());
    }

    private void tvSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void tvForgotPasswordClick() {

    }

    private void btnLoginClick() {
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPass.getText().toString().trim();

        // Validate input
        if (!isValidInput(email, password)) {
            return;
        }

        User user = userPresenter.getUserFindByEmail(email);
        if (user != null && user.getPassword() != null  && !user.getDelete() && user.getPassword().equals(password)) {
            if (user.getActive()) {
                SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(LoginActivity.this);
                sharedPreferenceManager.setStringValue(SharedPreferenceManager.AUTH_TOKEN, "mock_auth_token");
                switchToHomeActivity();
            } else {
                String code = SendMail.getRandom();
                user.setCode(code);
                //userPresenter.updateUser(user);
                String subject = "Login Verification Code";
                String message = "Your verification code is: " + code + "\n" +
                        "Please enter this code to proceed.";

                if (SendMail.sendEmail(email, subject, message)) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationVerificationActivity.class);
                    intent.putExtra("user", user);
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
        if (email.isEmpty()) {
            binding.edEmail.setError("Please enter your email!");
            binding.edEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Invalid email!");
            binding.edEmail.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            binding.edPass.setError("Please enter your password!");
            binding.edPass.requestFocus();
            return false;
        }
        return true;
    }

    private void switchToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}