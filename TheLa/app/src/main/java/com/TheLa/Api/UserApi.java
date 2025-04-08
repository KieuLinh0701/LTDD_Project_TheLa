package com.TheLa.Api;

import com.TheLa.dto.LoginDto;
import com.TheLa.dto.RegisterDto;
import com.TheLa.dto.ResetPasswordDto;
import com.TheLa.dto.UserDto;
import com.TheLa.dto.VerifyAccountDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {
    @POST("/register")
    Call<ApiResponse> register(@Body RegisterDto registerDto);

    @POST("/login")
    Call<UserDto> login(@Body LoginDto loginDto);

    @GET("/send-email-verify-account")
    Call<ApiResponse> sendEmailVerifyAccount(@Query("email") String email, @Query("feature") String feature);

    @PUT("/verify-account")
    Call<ApiResponse> verifyAccount(@Body VerifyAccountDto verifyAccountDto);

    @PUT("/reset-password")
    Call<ApiResponse> resetPassword(@Body ResetPasswordDto resetPasswordDto);
}
