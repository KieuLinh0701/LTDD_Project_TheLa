package com.TheLa.Api;

import com.TheLa.dto.ProductDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("/products")
    Call<List<ProductDto>> getAllActiveAndNotDeletedProducts();

    @GET("/products/by-category")
    Call<List<ProductDto>> getActiveAndNotDeletedProducts(@Query("categoryId") Long categoryId);

    @GET("/products/best-seller")
    Call<List<ProductDto>> getTop10BestSellingActiveAndNotDeletedProducts();

    @GET("/products/latest")
    Call<List<ProductDto>> get10RecentActiveAndNotDeletedProducts();
}
