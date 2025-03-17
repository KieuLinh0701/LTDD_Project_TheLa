package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;
import com.TheLa.dao.IProductDao;
import com.TheLa.services.implement.ProductImageService;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDao {
    ProductImageService productImageService = new ProductImageService();
    @Override
    public List<ProductModel> getAllActiveAndNotDeletedProducts() {
        List<ProductModel> productModels = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM products WHERE isActive = 1 AND isDelete = 0";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        ProductModel productModel = new ProductModel();
                        productModel.setProductId(resultSet.getLong("productId"));
                        productModel.setCategoryId(resultSet.getLong("categoryId"));
                        productModel.setName(resultSet.getString("name"));
                        productModel.setDescription(resultSet.getString("description"));
                        productModel.setCreateDate(resultSet.getTimestamp("createDate"));
                        productModel.setStatus(resultSet.getBoolean("status"));
                        productModel.setActive(resultSet.getBoolean("isActive"));
                        productModel.setDelete(resultSet.getBoolean("isDelete"));

                        List<ProductImageModel> listProductImageModel = productImageService.findProductImageByProduct(productModel.getProductId());
                        productModel.setListProductImageModelList(listProductImageModel);

                        productModels.add(productModel);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productModels;
    }

    @Override
    public List<ProductModel> get10RecentActiveAndNotDeletedProducts() {
        List<ProductModel> productModels = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT TOP 10 * FROM products \n" +
                        "WHERE isActive = 1 AND isDelete = 0 AND DATEDIFF(DAY, createDate, GETDATE()) <= 7 \n" +
                        "ORDER BY createDate DESC";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        ProductModel productModel = new ProductModel();
                        productModel.setProductId(resultSet.getLong("productId"));
                        productModel.setCategoryId(resultSet.getLong("categoryId"));
                        productModel.setName(resultSet.getString("name"));
                        productModel.setDescription(resultSet.getString("description"));
                        productModel.setCreateDate(resultSet.getTimestamp("createDate"));
                        productModel.setStatus(resultSet.getBoolean("status"));
                        productModel.setActive(resultSet.getBoolean("isActive"));
                        productModel.setDelete(resultSet.getBoolean("isDelete"));

                        List<ProductImageModel> listProductImageModel = productImageService.findProductImageByProduct(productModel.getProductId());
                        productModel.setListProductImageModelList(listProductImageModel);

                        productModels.add(productModel);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productModels;
    }

    @Override
    public List<ProductModel> getTop10BestSellingActiveAndNotDeletedProducts() {
        List<ProductModel> productModels = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT TOP 10 p.*\n" +
                        "FROM products p\n" +
                        "INNER JOIN order_details o\n" +
                        "ON p.productId = o.productId\n" +
                        "WHERE p.isActive = 1 AND p.isDelete = 0\n" +
                        "GROUP BY p.productId, p.categoryId, p.name, p.price, p.image, p.description, p.createDate, p.status, p.isActive, p.isDelete\n" +
                        "ORDER BY SUM(o.quantity) DESC;";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        ProductModel productModel = new ProductModel();
                        productModel.setProductId(resultSet.getLong("productId"));
                        productModel.setCategoryId(resultSet.getLong("categoryId"));
                        productModel.setName(resultSet.getString("name"));
                        productModel.setDescription(resultSet.getString("description"));
                        productModel.setCreateDate(resultSet.getTimestamp("createDate"));
                        productModel.setStatus(resultSet.getBoolean("status"));
                        productModel.setActive(resultSet.getBoolean("isActive"));
                        productModel.setDelete(resultSet.getBoolean("isDelete"));

                        List<ProductImageModel> listProductImageModel = productImageService.findProductImageByProduct(productModel.getProductId());
                        productModel.setListProductImageModelList(listProductImageModel);

                        productModels.add(productModel);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productModels;
    }

    @Override
    public List<ProductModel> findActiveAndNotDeletedProductsByCategoryId(long categoryId) {
        List<ProductModel> productModels = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM products \n" +
                        "WHERE isActive = 1 AND isDelete = 0 AND categoryId = ? \n" +
                        "ORDER BY createDate DESC";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    // Đặt tham số vào câu lệnh SQL
                    preparedStatement.setLong(1, categoryId);

                    // Thực hiện truy vấn và xử lý kết quả
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            ProductModel productModel = new ProductModel();
                            productModel.setProductId(resultSet.getLong("productId"));
                            productModel.setCategoryId(resultSet.getLong("categoryId"));
                            productModel.setName(resultSet.getString("name"));
                            productModel.setDescription(resultSet.getString("description"));
                            productModel.setCreateDate(resultSet.getTimestamp("createDate"));
                            productModel.setStatus(resultSet.getBoolean("status"));
                            productModel.setActive(resultSet.getBoolean("isActive"));
                            productModel.setDelete(resultSet.getBoolean("isDelete"));

                            List<ProductImageModel> listProductImageModel = productImageService.findProductImageByProduct(productModel.getProductId());
                            productModel.setListProductImageModelList(listProductImageModel);

                            productModels.add(productModel);
                        }
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productModels;
    }
}

