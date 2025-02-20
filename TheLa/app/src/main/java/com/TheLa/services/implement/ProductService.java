package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.Product;
import com.TheLa.dao.implement.ProductDao;
import com.TheLa.services.IProductService;

import java.util.List;

public class ProductService extends ViewModel implements IProductService {
    private final ProductDao productDao;

    public ProductService() {
        productDao = new ProductDao();
    }

    @Override
    public List<Product> getAllActiveAndNotDeletedProducts() {
        return productDao.getAllActiveAndNotDeletedProducts();
    }

    @Override
    public List<Product> get10RecentActiveAndNotDeletedProducts() {
        return productDao.get10RecentActiveAndNotDeletedProducts();
    }

    @Override
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId) {
        return productDao.findActiveAndNotDeletedProductsByCategoryId(categoryId);
    }

    @Override
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts() {
        return productDao.getTop10BestSellingActiveAndNotDeletedProducts();
    }
}
