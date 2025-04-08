package com.TheLa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.TheLa.dto.UserDto;
import com.google.gson.Gson;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "TheLaAppPrefs";
    private static final String KEY_USER = "USER";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Lưu UserDto dưới dạng JSON
    public void saveUser(UserDto user) {
        String userJson = gson.toJson(user);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    // Lấy UserDto từ SharedPreferences
    public UserDto getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        return userJson != null ? gson.fromJson(userJson, UserDto.class) : null;
    }

    // Xóa thông tin người dùng (khi logout)
    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }

    // Kiểm tra đã đăng nhập chưa
    public boolean isLoggedIn() {
        return getUser() != null;
    }
}
