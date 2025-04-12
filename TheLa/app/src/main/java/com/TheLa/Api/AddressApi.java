package com.TheLa.Api;

import com.TheLa.dto.AddressDto;
import com.TheLa.dto.CategoryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddressApi {
    @POST("/addresses")
    Call<AddressDto> newAddress(@Body AddressDto dto, @Query("userId") Long userId);

    @PUT("/addresses")
    Call<ApiResponse> updateAddress(@Body AddressDto dto);
}
