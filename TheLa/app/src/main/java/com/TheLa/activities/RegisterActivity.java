package com.TheLa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.TheLa.utils.Constant;
import com.TheLa.utils.JsonEncryptor;
import com.TheLa.utils.PasswordUtils;
import com.example.TheLa.R;
import com.example.TheLa.databinding.ActivityRegisterBinding;

import org.json.JSONObject;

import java.sql.Timestamp;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    UserService userService;
    private boolean isPasswordVisible = false;
    private boolean isRepeatPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        userService = new ViewModelProvider(this).get(UserService.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

        String code = SendMail.getRandom();

        try {
            String hashedPassword = PasswordUtils.hashPassword(password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("otp", code);
            String encryptedCode = JsonEncryptor.encrypt(jsonObject.toString());

            User user = new User(
                    name,
                    email,
                    hashedPassword, // Lưu mật khẩu đã mã hóa
                    encryptedCode, // Lưu code đã mã hóa
                    null,
                    null,
                    Constant.ROLE_CUSTOMER,
                    null,
                    new Timestamp(System.currentTimeMillis()),
                    false
            );

            String subject  = "Xác Thực Tài Khoản";
            String message = "Hi " + user.getName() + ",\n" +
                    "Hãy sử dụng mã bên dưới để xác nhận tài khoản của bạn:\n\n" +
                    code + "\n\n" +
                    "Cảm ơn bạn đã tham gia cùng chúng tôi!";

            if (SendMail.sendEmail(email, subject, message)) {
                User newUser = userService.addUser(user);
                if (newUser != null) {
                    Intent intent = new Intent(RegisterActivity.this, VerificationAccountActivity.class);
                    intent.putExtra("user", newUser);
                    intent.putExtra("feature", "Register");
                    startActivity(intent);
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi khi gửi email. Vui lòng kiểm tra địa chỉ email hoặc kết nối mạng của bạn và thử lại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi khi mã hóa dữ liệu!", Toast.LENGTH_SHORT).show();
        }
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
        } else if (userService.getUserFindByEmail(email) != null) {
            binding.etEmail.setError("Email này đã được liên kết với một tài khoản khác!");
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