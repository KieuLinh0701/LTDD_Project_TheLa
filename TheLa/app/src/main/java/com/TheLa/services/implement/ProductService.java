package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.ProductModel;
import com.TheLa.dao.implement.ProductDao;
import com.TheLa.services.IProductService;

import java.util.List;

public class ProductService extends ViewModel implements IProductService {
    private final ProductDao productDao;

    public ProductService() {
        productDao = new ProductDao();
    }

    @Override
    public List<ProductModel> getAllActiveAndNotDeletedProducts() {
        return productDao.getAllActiveAndNotDeletedProducts();
    }

    @Override
    public List<ProductModel> get10RecentActiveAndNotDeletedProducts() {
        return productDao.get10RecentActiveAndNotDeletedProducts();
    }

    @Override
    public List<ProductModel> findActiveAndNotDeletedProductsByCategoryId(long categoryId) {
        return productDao.findActiveAndNotDeletedProductsByCategoryId(categoryId);
    }

    @Override
    public List<ProductModel> getTop10BestSellingActiveAndNotDeletedProducts() {
        return productDao.getTop10BestSellingActiveAndNotDeletedProducts();
    }
}
