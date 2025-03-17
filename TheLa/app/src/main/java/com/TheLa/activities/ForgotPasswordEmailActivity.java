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

import com.TheLa.models.UserModel;
import com.TheLa.services.implement.UserService;
import com.TheLa.configs.SendMail;
import com.TheLa.utils.JsonEncryptor;
import com.example.TheLa.databinding.ActivityForgotpasswordEmailBinding;

import org.json.JSONObject;

import java.sql.Timestamp;

public class ForgotPasswordEmailActivity extends AppCompatActivity {
    ActivityForgotpasswordEmailBinding binding;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpasswordEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

        UserModel user = userService.getUserFindByEmail(email);
        if (user != null) {

            try {
                String code = SendMail.getRandom();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("otp", code);
                String encryptedCode = JsonEncryptor.encrypt(jsonObject.toString());
                user.setCode(encryptedCode);
                user.setCreateCode(new Timestamp(System.currentTimeMillis()));
                String subject = "Đặt Lại Mật Khẩu";
                String message = "Hi " + user.getName() + ",\n" +
                        "Hãy sử dụng mã bên dưới để đặt lại mật khẩu của bạn:\n\n" +
                        code + "\n\n" +
                        "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.";

                if (SendMail.sendEmail(email, subject, message)) {
                    if (userService.updateUser(user)) {
                        Intent intent = new Intent(ForgotPasswordEmailActivity.this, VerificationAccountActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("feature", "ForgotPassword");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ForgotPasswordEmailActivity.this, "Gửi email xác thực không thành công. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(ForgotPasswordEmailActivity.this, "Đã xảy ra lỗi khi tạo hoặc gửi mã xác thực!", Toast.LENGTH_SHORT).show();
                e.printStackTrace(); // In lỗi ra log để hỗ trợ debug
            }
        } else {
            Toast.makeText(ForgotPasswordEmailActivity.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
        }
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