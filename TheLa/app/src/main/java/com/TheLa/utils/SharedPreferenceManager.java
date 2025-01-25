package com.TheLa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    public static final String THE_LA_REFERENCE_NAME = "TheLa";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "email";
    public static final String USER_ROLE = "role";
    public static final String AUTH_TOKEN = "authToken";
    public static final String CART_ITEMS = "cart_items";

    private Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
    }

    public void saveLoginInfo (String deliveryName, String authToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, deliveryName);
        editor.putString(AUTH_TOKEN, authToken);
        editor.apply();
    }


    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public String getStringValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setLongValue(String key, Long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public float getFloatValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0f);
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public Long getLongValue(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THE_LA_REFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0L);
    }
}
