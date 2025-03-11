package com.TheLa.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class JsonEncryptor {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "TheLaSecureKey12".getBytes(); // Khóa bí mật (16 bytes)

    // Hàm mã hóa
    public static String encrypt(String data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT); // Sử dụng android.util.Base64
    }

    // Hàm giải mã
    public static String decrypt(String encryptedData) throws Exception {
        SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT); // Sử dụng android.util.Base64
        return new String(cipher.doFinal(decodedData));
    }
}