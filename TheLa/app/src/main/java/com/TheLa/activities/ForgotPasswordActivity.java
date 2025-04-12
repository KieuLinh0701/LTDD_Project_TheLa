package com.TheLa.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.dto.ResetPasswordDto;
import com.TheLa.fragments.MeFragment;
import com.TheLa.utils.AppUtils;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityForgotpasswordBinding;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotpasswordBinding binding;
    private String feature, email;
    private boolean isPasswordVisible = false;
    private boolean isRepeatPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");
        feature = getIntent().getStringExtra("feature");

        addEvents();
    }

    private void addEvents() {
        binding.btnRecovery.setOnClickListener(v -> btnClick());
        binding.btnVisiblePassword.setOnClickListener(
                v -> btnVisiblePasswordClick()
        );

        binding.btnVisibleRepeatPassword.setOnClickListener(
                v -> btnVisibleRepeatPasswordClick()
        );
        binding.btnBack.setOnClickListener(
                v -> btnBackClick()
        );
    }

    private void btnBackClick() {
        if ("ForgotPassword".equals(feature)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
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

    private void btnClick() {
        String pass = binding.etPassword.getText().toString().trim();
        String rePass = binding.etRepeatPassword.getText().toString().trim();

        if (!isValidInput(pass, rePass)) {
            return;
        }

        ResetPasswordDto resetPasswordDto = new ResetPasswordDto(email, pass);
        CallApiResetPassword(resetPasswordDto);
    }

    private void CallApiResetPassword(ResetPasswordDto resetPasswordDto) {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        Call<ApiResponse> call = userApi.resetPassword(resetPasswordDto);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("LoginActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    if ("ForgotPassword".equals(feature)) {
                        Toast.makeText(ForgotPasswordActivity.this, "Khôi phục mật khẩu thành công! Đang chuyển hướng đến trang đăng nhập...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }, 2000);
                    } else if ("ChangePassword".equals(feature)) {
                        Toast.makeText(ForgotPasswordActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("action", "backToMeFragment");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }, 2000);
                    }
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(ForgotPasswordActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Lỗi xử lý mật khẩu. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi xử lý mật khẩu. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("ForgotPasswordActivity", "Lỗi xử lý mật khẩu. Vui lòng thử lại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ForgotPasswordActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput(String password, String rePassword) {
        boolean isValid = true;
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