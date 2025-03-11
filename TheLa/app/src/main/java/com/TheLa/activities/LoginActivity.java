package com.TheLa.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.models.User;
import com.TheLa.services.implement.UserService;
import com.TheLa.configs.SendMail;
import com.TheLa.utils.JsonEncryptor;
import com.TheLa.utils.PasswordUtils;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import java.sql.Timestamp;

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

//         SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
//         if (sharedPreferenceManager.getStringValue(SharedPreferenceManager.AUTH_TOKEN) != null) {
//            switchToHomeActivity();
//         }

        addEvents();
    }

    private void addEvents() {
        binding.btnLogin.setOnClickListener(v -> btnLoginClick());
        binding.tvForgotPassword.setOnClickListener(v -> tvForgotPasswordClick());
        binding.tvSignUp.setOnClickListener(v -> tvSignUpClick());
        binding.btnVisiblePassword.setOnClickListener(v -> btnVisiblePasswordClick());
        binding.btnBack.setOnClickListener(
                v -> btnBackClick()
        );
    }

    private void btnBackClick() {
        finish();
    }

    private void btnVisiblePasswordClick() {
        if (isPasswordVisible) {
            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.btnVisiblePassword.setImageResource(R.drawable.ic_eye_closed);
        } else {
            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.btnVisiblePassword.setImageResource(R.drawable.ic_eye_open);
        }

        binding.etPassword.setTypeface(Typeface.SANS_SERIF);
        binding.etPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        binding.btnVisiblePassword.setColorFilter(ContextCompat.getColor(this, R.color.green));

        binding.etPassword.setSelection(binding.etPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
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
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Validate input
        if (!isValidInput(email, password)) {
            return;
        }

        User user = userService.getUserFindByEmail(email);
        if (user != null && user.getPassword() != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
            if (user.getActive()) {
                SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(LoginActivity.this);
                sharedPreferenceManager.setStringValue(SharedPreferenceManager.AUTH_TOKEN, "mock_auth_token");
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                switchToHomeActivity();
            } else {
                try {
                    String code = SendMail.getRandom();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("otp", code);
                    String encryptedCode = JsonEncryptor.encrypt(jsonObject.toString());
                    user.setCode(encryptedCode);
                    user.setCreateCode(new Timestamp(System.currentTimeMillis()));
                    String subject = "Xác Thực Tài Khoản";
                    String message = "Hi " + user.getName() + ",\n" +
                            "Hãy sử dụng mã bên dưới để xác nhận tài khoản của bạn:\n\n" +
                            code + "\n\n" +
                            "Cảm ơn bạn đã tham gia cùng chúng tôi!";

                    if (SendMail.sendEmail(email, subject, message)) {
                        Toast.makeText(this, "Tài khoản của bạn chưa được xác thực. Vui lòng kiểm tra email và hoàn tất xác thực.", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(LoginActivity.this, VerificationAccountActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("feature", "Login");
                            startActivity(intent);
                        }, 2000);
                    } else {
                        Toast.makeText(LoginActivity.this, "Gửi email xác thực không thành công. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi khi xử lý mã xác thực. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, vui lòng kiểm tra email hoặc mật khẩu của bạn!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput(String email, String password) {
        boolean isValid = true;
        if (email.isEmpty()) {
            binding.etEmail.setError("Vui lòng nhập email!");
            binding.etEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Email không hợp lệ!");
            binding.etEmail.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Vui lòng nhập mật khẩu!");
            binding.etPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void switchToHomeActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }, 1000);
    }

}