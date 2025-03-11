package com.TheLa.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {
    // Hàm mã hóa mật khẩu
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 là độ mạnh (strength)
    }

    // Hàm kiểm tra mật khẩu
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}