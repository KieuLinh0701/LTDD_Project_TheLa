package com.TheLa.dao;

import com.TheLa.models.Product;

import java.util.List;

public interface IProductDao {
    List<Product> getAllActiveAndNotDeletedProducts();
    List<Product> get10RecentActiveAndNotDeletedProducts();
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId);
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts();
}
