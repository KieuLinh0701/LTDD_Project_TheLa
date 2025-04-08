package com.TheLa.Api;

import com.TheLa.dto.CategoryDto;
import com.TheLa.dto.PromotionDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PromotionApi {
    @GET("/promotions")
    Call<List<PromotionDto>> getAllActiveAndNotDeletedPromotions();
}
