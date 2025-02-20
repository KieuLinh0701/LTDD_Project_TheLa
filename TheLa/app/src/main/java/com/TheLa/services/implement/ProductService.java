package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.Product;
import com.TheLa.repository.implement.ProductRepository;
import com.TheLa.services.IProductService;

import java.util.Collections;
import java.util.List;

public class ProductService extends ViewModel implements IProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        productRepository = new ProductRepository();
    }

    @Override
    public List<Product> getAllActiveAndNotDeletedProducts() {
        return productRepository.getAllActiveAndNotDeletedProducts();
    }

    @Override
    public List<Product> get10RecentActiveAndNotDeletedProducts() {
        return productRepository.get10RecentActiveAndNotDeletedProducts();
    }

    @Override
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId) {
        return productRepository.findActiveAndNotDeletedProductsByCategoryId(categoryId);
    }

    @Override
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts() {
        return productRepository.getTop10BestSellingActiveAndNotDeletedProducts();
    }
}
