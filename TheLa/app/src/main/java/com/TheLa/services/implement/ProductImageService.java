package com.TheLa.services.implement;

import android.util.Log;

import com.TheLa.dao.IProductImageDao;
import com.TheLa.dao.implement.ProductDao;
import com.TheLa.dao.implement.ProductImageDao;
import com.TheLa.models.ProductImageModel;
import com.TheLa.services.IProductImageService;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductImageService implements IProductImageService {

    private final ProductImageDao productImageDao;

    public ProductImageService() {
        productImageDao = new ProductImageDao();
    }
    @Override
    public List<ProductImageModel> findProductImageByProduct(Long productId) {
        return productImageDao.findProductImageByProduct(productId);
    }
}
