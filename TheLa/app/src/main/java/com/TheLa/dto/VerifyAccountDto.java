package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyAccountDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("email")
    private String email;

    @SerializedName("code")
    private String code;

    @SerializedName("otpDuration")
    private int otpDuration;

    public VerifyAccountDto(String email, String code, int otpDuration) {
        this.email = email;
        this.code = code;
        this.otpDuration = otpDuration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOtpDuration() {
        return otpDuration;
    }

    public void setOtpDuration(int otpDuration) {
        this.otpDuration = otpDuration;
    }
}

