package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.TheLa.configs.SendMail;
import com.TheLa.models.User;
import com.TheLa.services.implement.UserService;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityVerificationAccountBinding;

import java.sql.Timestamp;

public class VerificationAccountActivity extends AppCompatActivity {
    private int otpDuration = 180;
    ActivityVerificationAccountBinding binding;
    UserService userService;
    EditText[] otpInputs;
    String feature;
    private TextView tvTime, tvSendEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        feature = getIntent().getStringExtra("feature");

        if ("Login".equals(feature)) {
            binding.tvTitle.setText("Đăng nhập");
        } else if ("Register".equals(feature)) {
            binding.tvTitle.setText("Đăng ký");
        } else if ("ForgotPassword".equals(feature)) {
                binding.tvTitle.setText("Quên mật khẩu");
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
        User user = (User) getIntent().getSerializableExtra("user");

        String code = SendMail.getRandom();
        user.setCode(code);
        user.setCreateCode(new Timestamp(System.currentTimeMillis()));

        String subject = null;
        String message = null;
        if ("Login".equals(feature) || "Register".equals(feature)) {
            subject  = "Xác Thực Tài Khoản";
            message = "Hi " + user.getName() + ",\n" +
                    "Hãy sử dụng mã bên dưới để xác nhận tài khoản của bạn:\n\n" +
                    code + "\n\n" +
                    "Cảm ơn bạn đã tham gia cùng chúng tôi!";
        } else if ("ForgotPassword".equals(feature)) {
            subject = "Đặt Lại Mật Khẩu";
            message = "Hi " + user.getName() + ",\n" +
                    "Hãy sử dụng mã bên dưới để đặt lại mật khẩu của bạn:\n\n" +
                    code + "\n\n" +
                    "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.";
        }

        if (!SendMail.sendEmail(user.getEmail(), subject, message)) {
            Toast.makeText(VerificationAccountActivity.this, "Gửi email xác thực không thành công. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(VerificationAccountActivity.this, "Email xác thực đã được gửi!", Toast.LENGTH_SHORT).show();
            tvSendEmail.setEnabled(false);
            tvSendEmail.setTextColor(ContextCompat.getColor(VerificationAccountActivity.this, R.color.dark_gray));
            startCountdown();
        }
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
        }

        finish();
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
                tvSendEmail.setEnabled(true);
                tvSendEmail.setText("Gửi lại");
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

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null && user.getCreateCode() != null &&
                ((System.currentTimeMillis() - user.getCreateCode().getTime()) / 1000 <= otpDuration)) {
            if(code.equals(user.getCode())) {
                user.setActive(true);
                if (userService.updateUser(user)) {
                    if ("ForgotPassword".equals(feature)) {
                        Toast.makeText(this, "Xác thực thành công! Đang chuyển hướng...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(VerificationAccountActivity.this, ForgotPasswordActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish(); // Đóng activity hiện tại để không giữ nó trong ngăn xếp.
                        }, 2000);
                    } else if ("Login".equals(feature) || "Register".equals(feature)) {
                        Toast.makeText(this, "Xác thực thành công! Đang chuyển hướng đến trang đăng nhập...", Toast.LENGTH_SHORT).show();
                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(VerificationAccountActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }, 2000);
                    }
                }
            } else {
                Toast.makeText(this, "Mã OTP không chính xác, vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Mã OTP của bạn đã hết hạn. Vui lòng nhấn 'Gửi lại' để nhận mã mới và thử lại!", Toast.LENGTH_SHORT).show();
        }
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
