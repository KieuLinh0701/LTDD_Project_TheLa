package com.TheLa.Api;

import com.TheLa.dto.CategoryDto;
import com.TheLa.dto.ReviewDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReviewApi {
    @GET("/reviews")
    Call<List<ReviewDto>> getReviewsByProductId(@Query("productId") Long productId);
}
