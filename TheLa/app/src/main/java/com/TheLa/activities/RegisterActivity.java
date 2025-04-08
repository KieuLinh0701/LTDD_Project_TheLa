package com.TheLa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.TheLa.dto.RegisterDto;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityRegisterBinding;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private boolean isPasswordVisible = false;
    private boolean isRepeatPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {
        binding.btnRegister.setOnClickListener(
                v -> btnRegisterClick()
        );

        binding.tvLogIn.setOnClickListener(
                v -> btnLoginClick()
        );

        binding.btnVisiblePassword.setOnClickListener(
                v -> btnVisiblePasswordClick()
        );

        binding.btnVisibleRepeatPassword.setOnClickListener(
                v -> btnVisibleRepeatPasswordClick()
        );

        binding.btnBack.setOnClickListener(
                v -> btnBack()
        );
    }

    private void btnBack() {
        finish();
    }

    private void btnVisibleRepeatPasswordClick() {
        if (isRepeatPasswordVisible) {
            binding.etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.btnVisibleRepeatPassword.setImageResource(R.drawable.ic_eye_closed);
        } else {
            binding.etRepeatPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.btnVisibleRepeatPassword.setImageResource(R.drawable.ic_eye_open);
        }

        binding.etRepeatPassword.setTypeface(Typeface.SANS_SERIF);
        binding.etRepeatPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        binding.btnVisibleRepeatPassword.setColorFilter(ContextCompat.getColor(this, R.color.green));

        binding.etRepeatPassword.setSelection(binding.etRepeatPassword.getText().length());
        isRepeatPasswordVisible = !isRepeatPasswordVisible;
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

        RegisterDto registerDto = new RegisterDto(name, email, password);
        callAPI(registerDto);
    }

    private void callAPI(RegisterDto registerDto) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);


        Call<ApiResponse> call = userApi.register(registerDto);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("RegisterActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, VerificationAccountActivity.class);
                        intent.putExtra("email", registerDto.getEmail());
                        intent.putExtra("feature", "Register");
                        startActivity(intent);
                    }, 2000);
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(RegisterActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("RegisterActivity", "Đăng ký tài khoản thất bại!" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("RegisterActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String name, String email, String password, String rePassword) {
        boolean isValid = true;
        if (name.isEmpty()) {
            binding.etName.setError("Vui lòng nhập tên!");
            binding.etName.requestFocus();
            isValid = false;
        }

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
        } else if (!isPasswordStrong(password)) {
            isValid = false;
        }

        if (rePassword.isEmpty()) {
            binding.etRepeatPassword.setError("Vui lòng xác nhận lại mật khẩu!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        } else if (!password.equals(rePassword)) {
            binding.etRepeatPassword.setError("Mật khẩu không khớp!");
            binding.etRepeatPassword.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
        if (password.length() < 8) {
            binding.etPassword.setError("Mật khẩu phải có ít nhất 8 ký tự!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái hoa
        if (!password.matches(".*[A-Z].*")) {
            binding.etPassword.setError("Mật khẩu phải chứa ít nhất một chữ cái viết hoa!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một chữ cái thường
        if (!password.matches(".*[a-z].*")) {
            binding.etPassword.setError("Mật khẩu phải chứa ít nhất một chữ cái viết thường!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một số
        if (!password.matches(".*[0-9].*")) {
            binding.etPassword.setError("Mật khẩu phải chứa ít nhất một chữ số!");
            binding.etPassword.requestFocus();
            return false;
        }

        // Kiểm tra có ít nhất một ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>_].*")) {
            binding.etPassword.setError("Mật khẩu phải chứa ít nhất một ký tự đặc biệt!");
            binding.etPassword.requestFocus();
            return false;
        }

        return true;
    }
}