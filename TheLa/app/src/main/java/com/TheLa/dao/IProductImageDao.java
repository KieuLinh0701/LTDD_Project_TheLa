package com.TheLa.dao;

import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;

import java.util.List;

public interface IProductImageDao {
    List<ProductImageModel> findProductImageByProduct(Long ProductId);
}
