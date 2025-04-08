package com.TheLa.Api;

import com.TheLa.dto.CategoryDto;
import com.TheLa.dto.OrderDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderApi {
    @GET("/orders")
    Call<List<OrderDto>> getOrders(@Query("userId") Long userId,
                                   @Query("status") String status);

    @GET("/orders/search")
    Call<List<OrderDto>> getOrdersBySearch(@Query("userId") Long userId,
                                   @Query("word") String word);
}
