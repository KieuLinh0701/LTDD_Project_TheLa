package com.TheLa.dao;

import com.TheLa.models.ProductModel;

import java.util.List;

public interface IProductDao {
    List<ProductModel> getAllActiveAndNotDeletedProducts();
    List<ProductModel> get10RecentActiveAndNotDeletedProducts();
    public List<ProductModel> findActiveAndNotDeletedProductsByCategoryId(long categoryId);
    public List<ProductModel> getTop10BestSellingActiveAndNotDeletedProducts();
}
