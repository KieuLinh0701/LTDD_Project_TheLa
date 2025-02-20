package com.TheLa.services;

import com.TheLa.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllActiveAndNotDeletedProducts();
    List<Product> get10RecentActiveAndNotDeletedProducts();
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId);
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts();
}
