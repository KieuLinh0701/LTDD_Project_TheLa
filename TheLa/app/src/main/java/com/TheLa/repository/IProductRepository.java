package com.TheLa.repository;

import com.TheLa.models.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> getAllActiveAndNotDeletedProducts();
    List<Product> get10RecentActiveAndNotDeletedProducts();
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId);
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts();
}
