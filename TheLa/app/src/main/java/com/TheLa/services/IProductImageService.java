package com.TheLa.services;

import com.TheLa.models.ProductImageModel;

import java.util.List;

public interface IProductImageService {
    List<ProductImageModel> findProductImageByProduct(Long ProductId);
}
