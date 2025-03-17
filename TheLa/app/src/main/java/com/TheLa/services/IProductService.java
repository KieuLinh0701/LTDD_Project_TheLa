package com.TheLa.services;

import com.TheLa.models.ProductModel;

import java.util.List;

public interface IProductService {
    List<ProductModel> getAllActiveAndNotDeletedProducts();
    List<ProductModel> get10RecentActiveAndNotDeletedProducts();
    public List<ProductModel> findActiveAndNotDeletedProductsByCategoryId(long categoryId);
    public List<ProductModel> getTop10BestSellingActiveAndNotDeletedProducts();
}
