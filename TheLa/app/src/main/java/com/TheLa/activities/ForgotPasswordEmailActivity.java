package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.presenter.UserPresenter;
import com.TheLa.services.SendMail;
import com.example.TheLa.databinding.ActivityForgotpasswordEmailBinding;
import com.example.TheLa.databinding.ActivityLoginBinding;

public class ForgotPasswordEmailActivity extends AppCompatActivity {
    ActivityForgotpasswordEmailBinding binding;
    UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userPresenter = new ViewModelProvider(this).get(UserPresenter.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addEvents();
    }

    private void addEvents() {
        binding.btnNext.setOnClickListener(v -> btnNextClick());
    }

    private void btnNextClick() {
        String email = binding.edEmail.getText().toString().trim();

        // Validate input
        if (!isValidInput(email)) {
            return;
        }

        User user = userPresenter.getUserFindByEmail(email);
        if (user != null) {
            String code = SendMail.getRandom();
            user.setCode(code);
            String subject = "Reset Your Password";
            String message = "Hi " + user.getName() + ",\n\n" +
                    "We received a request to reset your password for your account. Please use the verification code below to reset your password:\n\n" +
                    code + "\n\n" +
                    "If you did not request a password reset, please ignore this email or contact support if you have concerns.\n\n" +
                    "Thank you,\n" +
                    "The La Team";

            if (SendMail.sendEmail(email, subject, message)) {
                if (userPresenter.updateUser(user)) {
                    Intent intent = new Intent(ForgotPasswordEmailActivity.this, VerificationAccountActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("feature", "ForgotPassword");
                    startActivity(intent);
                }
            } else {
                Toast.makeText(ForgotPasswordEmailActivity.this, "Failed to send the verification email. Please try again later!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ForgotPasswordEmailActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput(String email) {
        if (email.isEmpty()) {
            binding.edEmail.setError("Please enter your email!");
            binding.edEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.setError("Invalid email!");
            binding.edEmail.requestFocus();
            return false;
        }
        return true;
    }
}