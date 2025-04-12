package com.TheLa.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.dto.LoginDto;
import com.TheLa.dto.UserDto;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        LoginDto loginDto = new LoginDto(email, password);
        CallApiLogin(loginDto);
    }

    private void CallApiLogin(LoginDto loginDto) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        Call<UserDto> call = userApi.login(loginDto);

        call.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                Log.d("LoginActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    UserDto userDto = response.body();
                    if (!userDto.getActive()) {
                        callAPISendEmail(loginDto.getEmail());
                    } else {
                        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(LoginActivity.this);
                        preferenceManager.saveUser(userDto);

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Đang chuyển hướng đến...", Toast.LENGTH_SHORT).show();
                        switchToHomeActivity();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, vui lòng kiểm tra email hoặc mật khẩu của bạn!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "Đăng nhập thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Log.e("LoginActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callAPISendEmail(String email) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        Call<ApiResponse> call = userApi.sendEmailVerifyAccount(email, "Login");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("LoginActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Tài khoản của bạn chưa được xác thực. Vui lòng kiểm tra email và hoàn tất xác thực!", Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, VerificationAccountActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("feature", "Login");
                        startActivity(intent);
                    }, 2000);
                } else {
                    Log.d("LoginActivity", "Gửi email xác thực tài khoản thất bại: " + response.message());
                    Toast.makeText(LoginActivity.this, "Gửi email xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("VerificationAccountActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
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
        String previousPage = getIntent().getStringExtra("previousPage");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            if (previousPage != null) {
                Log.e("Login Activity", previousPage);
                intent.putExtra("navigateTo", previousPage); // Gửi thông tin về trang cần chuyển đến
            } else {
                Log.e("Login Activity", "Không có");
            }
            startActivity(intent);
        }, 0);
    }


}