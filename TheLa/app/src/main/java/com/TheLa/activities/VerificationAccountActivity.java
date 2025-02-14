package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.presenter.UserPresenter;
import com.example.TheLa.databinding.ActivityVerificationAccountBinding;

public class VerificationAccountActivity extends AppCompatActivity {
    ActivityVerificationAccountBinding binding;
    UserPresenter userPresenter;
    EditText[] otpInputs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userPresenter = new ViewModelProvider(this).get(UserPresenter.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if ("Login".equals(getIntent().getStringExtra("feature"))) {
            binding.title.setText("Verify\nAccount");
        } else if ("ForgotPassword".equals(getIntent().getStringExtra("feature"))) {
            binding.title.setText("Forgot\nPassword");
        }

        otpInputs = new EditText[]{
                binding.ettEmail1,
                binding.ettEmail2,
                binding.ettEmail3,
                binding.ettEmail4,
                binding.ettEmail5,
                binding.ettEmail6
        };

        for (int i = 0; i < otpInputs.length; i++) {
            int currentIndex = i;

            otpInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty() && currentIndex < otpInputs.length - 1) {
                        otpInputs[currentIndex + 1].requestFocus();
                    } else if (s.toString().isEmpty() && currentIndex > 0) {
                        otpInputs[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpInputs[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (otpInputs[currentIndex].getText().toString().isEmpty() && currentIndex > 0) {
                        otpInputs[currentIndex - 1].requestFocus();
                    }
                }
                return false;
            });
        }

        addEvents();
    }

    private void addEvents() {
        binding.btnVerify.setOnClickListener(v -> btnVerifyClick());
    }

    private void btnVerifyClick() {
        StringBuilder builderStr = new StringBuilder();

        builderStr.append(binding.ettEmail1.getText().toString().trim())
                .append(binding.ettEmail2.getText().toString().trim())
                .append(binding.ettEmail3.getText().toString().trim())
                .append(binding.ettEmail4.getText().toString().trim())
                .append(binding.ettEmail5.getText().toString().trim())
                .append(binding.ettEmail6.getText().toString().trim());

        String code = builderStr.toString();

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null && code.equals(user.getCode())) {
            user.setActive(true);
            if (userPresenter.updateUser(user)) {
                if ("ForgotPassword".equals(getIntent().getStringExtra("feature"))) {
                    Toast.makeText(this, "Verification successful! Redirecting...", Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent intent = new Intent(VerificationAccountActivity.this, ForgotPasswordActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish(); // Đóng activity hiện tại để không giữ nó trong ngăn xếp.
                    }, 2000);
                } else if ("Login".equals(getIntent().getStringExtra("feature"))
                        || "Register".equals(getIntent().getStringExtra("feature"))) {
                    Toast.makeText(this, "Verification successful! Redirecting to login page...", Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent intent = new Intent(VerificationAccountActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }, 2000);
                }
            }
        } else {
            Toast.makeText(this, "The verification code is incorrect, please check again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if ("ForgotPassword".equals(getIntent().getStringExtra("feature"))) {
            Intent intent = new Intent(VerificationAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
