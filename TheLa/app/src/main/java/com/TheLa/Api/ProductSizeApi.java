package com.TheLa.Api;

import com.TheLa.dto.ProductSizeDto;
import com.TheLa.dto.ReviewDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductSizeApi {
    @GET("/product-sizes")
    Call<List<ProductSizeDto>> getProductSizesByProductId(@Query("productId") Long productId);
}
