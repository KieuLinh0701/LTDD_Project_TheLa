package com.TheLa.Api;

import com.TheLa.dto.CategoryDto;
import com.TheLa.dto.ProductDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("/categories")
    Call<List<CategoryDto>> getAllActiveAndNotDeletedCategories();
}
