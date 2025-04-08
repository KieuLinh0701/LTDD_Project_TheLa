package com.TheLa.Api;

import com.TheLa.dto.ProductImageDto;
import com.TheLa.dto.ProductSizeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductImageApi {
    @GET("/product-images")
    Call<List<ProductImageDto>> getProductImagesByProductId(@Query("productId") Long productId);
}
