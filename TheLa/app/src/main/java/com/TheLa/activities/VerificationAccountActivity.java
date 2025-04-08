package com.TheLa.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.TheLa.Api.ApiClient;
import com.TheLa.Api.ApiResponse;
import com.TheLa.Api.UserApi;
import com.TheLa.dto.UserDto;
import com.TheLa.dto.VerifyAccountDto;
import com.TheLa.fragments.MeFragment;
import com.TheLa.utils.AppUtils;
import com.TheLa.utils.Constant;
import com.TheLa.utils.SharedPreferenceManager;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityVerificationAccountBinding;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationAccountActivity extends AppCompatActivity {
    private int otpDuration = Constant.OTP_DURATION;
    ActivityVerificationAccountBinding binding;
    EditText[] otpInputs;
    String feature, email;
    boolean clickTvSendEmail = false;
    private TextView tvTime, tvSendEmail;
    private UserDto user;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        feature = getIntent().getStringExtra("feature");
        email = getIntent().getStringExtra("email");

        if ("Login".equals(feature)) {
            binding.tvTitle.setText("Login");
        } else if ("Register".equals(feature)) {
            binding.tvTitle.setText("Sign Up");
        } else if ("ForgotPassword".equals(feature)) {
            binding.tvTitle.setText("Forgot Password");
        } else if ("ChangePassword".equals(feature)) {
            binding.tvTitle.setText("Change Password");
        } else if ("ChangeEmail".equals(feature)) {
            binding.tvTitle.setText("Change Email");
        }

        tvTime = binding.tvTime;
        tvSendEmail = binding.tvSendEmail;

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
        startCountdown();
        binding.btnBack.setOnClickListener(
                v -> btnBack()
        );
        binding.tvSendEmail.setOnClickListener(
                v -> tvSendEmailClick()
        );
    }

    private void tvSendEmailClick() {
        if (clickTvSendEmail) {
            callAPISendEmail();
        } else {
            String time = tvTime.getText().toString();

            String message = "Vui lòng đợi " + time + " giây trước khi gửi lại mã OTP.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void getUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(this);
        user = preferenceManager.getUser();
    }

    private void callAPISendEmail() {
        Log.e("EMAIL-FEATURE","Email: " + email + ", Feature: " + feature);
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);
        Call<ApiResponse> call;

        if ("ChangeEmail".equals(feature)) {
            getUser();
            call = userApi.sendEmailResetEmail(user.getEmail(), email);
        } else {
            call = userApi.sendEmailVerifyAccount(email, feature);
        }

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("VerifyAccountActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(VerificationAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    clickTvSendEmail = false;
                    tvSendEmail.setTextColor(ContextCompat.getColor(VerificationAccountActivity.this, R.color.dark_gray));
                    startCountdown();
                } else {
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(VerificationAccountActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerificationAccountActivity.this, "Gửi email xác thực tài khoản thất bại:", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(VerificationAccountActivity.this, "Gửi email xác thực tài khoản thất bại:", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("VerificationAccountActivity", "Gửi email xác thực tài khoản thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("VerificationAccountActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(VerificationAccountActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void btnBack() {
        if ("Login".equals(feature) || "ForgotPassword".equals(feature)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("Register".equals(feature)) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("ChangePassword".equals(feature)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "backToMeFragment"); // Thông báo xóa ChangePasswordFragment
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else if ("ChangeEmail".equals(feature)) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void startCountdown() {
        new CountDownTimer(otpDuration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTime.setText("0");
                clickTvSendEmail = true;
                //tvSendEmail.setText("Resend");
                tvSendEmail.setTextColor(ContextCompat.getColor(VerificationAccountActivity.this, R.color.red));
            }
        }.start();
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

        callAPIVerifyAccount(code);

    }

    private void callAPIVerifyAccount(String code) {
        Log.e("EMAIL-FEATURE","Email: " + email + ", Feature: " + feature);
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        VerifyAccountDto verifyAccountDto;

        if ("ChangeEmail".equals(feature)) {
            getUser();
            verifyAccountDto = new VerifyAccountDto(user.getEmail(), code, otpDuration);
        } else {
            verifyAccountDto = new VerifyAccountDto(email, code, otpDuration);
        }

        Log.d("VerifyAccountDto", "Email: " + email + ", Code: " + code + ", OTP Duration: " + otpDuration);

        Call<ApiResponse> call = userApi.verifyAccount(verifyAccountDto); // Sử dụng verifyAccount

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("VerifyAccountActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    if ("ForgotPassword".equals(feature) || "ChangePassword".equals(feature)) {
                        Toast.makeText(VerificationAccountActivity.this, "Xác thực thành công! Đang chuyển hướng...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(VerificationAccountActivity.this, ForgotPasswordActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("feature", feature);
                            startActivity(intent);
                            finish();
                        }, 2000);
                    } else if ("Register".equals(feature)) {
                        Toast.makeText(VerificationAccountActivity.this, "Xác thực thành công! Đang chuyển hướng đến trang đăng nhập...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(VerificationAccountActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }, 2000);
                    } else if ("Login".equals(feature)){
                        Toast.makeText(VerificationAccountActivity.this, "Xác thực thành công! Đang chuyển hướng đến trang chủ...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(VerificationAccountActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }, 2000);
                    } else if ("ChangeEmail".equals(feature)){
                        // gọi api lưu lại email
                        CallAPISaveUser();
                    }
                } else {
                    // Xử lý lỗi từ API (bao gồm BadRequest)
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(VerificationAccountActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerificationAccountActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(VerificationAccountActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("VerificationAccountActivity", "Xác thực tài khoản thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("VerificationAccountActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(VerificationAccountActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallAPISaveUser() {
        UserApi userApi = ApiClient.getRetrofitInstance().create(UserApi.class);

        user.setEmail(email);

        Call<ApiResponse> call = userApi.save(user);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("VerifyAccountActivity", "API phản hồi với mã: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    saveUser();

                    Toast.makeText(VerificationAccountActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    binding.getRoot().postDelayed(() -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("action", "backToMeFragment");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }, 2000);

                } else {
                    // Xử lý lỗi từ API (bao gồm BadRequest)
                    try {
                        ApiResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Toast.makeText(VerificationAccountActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerificationAccountActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(VerificationAccountActivity.this, "Xác thực tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("VerificationAccountActivity", "Xác thực tài khoản thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("VerificationAccountActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(VerificationAccountActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUser() {
        SharedPreferenceManager preferenceManager = new SharedPreferenceManager(VerificationAccountActivity.this);
        preferenceManager.saveUser(user);
    }

    @Override
    public void onBackPressed() {
        if ("ForgotPassword".equals(feature)) {
            Intent intent = new Intent(VerificationAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
