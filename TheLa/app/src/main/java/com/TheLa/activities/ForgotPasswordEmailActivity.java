package com.TheLa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.example.TheLa.databinding.ActivityForgotpasswordEmailBinding;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordEmailActivity extends AppCompatActivity {
    ActivityForgotpasswordEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
    }

    private void addEvents() {
        binding.btnNext.setOnClickListener(v -> btnNextClick());
        binding.btnBack.setOnClickListener(
                v -> btnBackClick()
        );
    }

    private void btnBackClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void btnNextClick() {
        String email = binding.etEmail.getText().toString().trim();

        // Validate input
        if (!isValidInput(email)) {
            return;
        }

        callAPISendEmail(email);

    }

    private void callAPISendEmail(String email) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        Call<ApiResponse> call = userApi.sendEmailVerifyAccount(email, "ForgotPassword");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("VerifyAccountActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ForgotPasswordEmailActivity.this, "Vui lòng kiểm tra email của bạn và nhập mã OTP để hoàn tất xác thực!", Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent intent = new Intent(ForgotPasswordEmailActivity.this, VerificationAccountActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("feature", "ForgotPassword");
                        startActivity(intent);
                    }, 2000);
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(ForgotPasswordEmailActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordEmailActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ForgotPasswordEmailActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("ForgotPasswordEmailActivity", "Xác thực tài khoản thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ForgotPasswordEmailActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(ForgotPasswordEmailActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String email) {
        if (email.isEmpty()) {
            binding.etEmail.setError("Vui lòng nhập email!");
            binding.etEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Email không hợp lệ!");
            binding.etEmail.requestFocus();
            return false;
        }
        return true;
    }
}