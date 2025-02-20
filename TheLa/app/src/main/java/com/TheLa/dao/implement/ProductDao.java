package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.models.Product;
import com.TheLa.dao.IProductDao;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDao {
    @Override
    public List<Product> getAllActiveAndNotDeletedProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM products WHERE isActive = 1 AND isDelete = 0";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        Product product = new Product();
                        product.setProductId(resultSet.getLong("productId"));
                        product.setCategoryId(resultSet.getLong("categoryId"));
                        product.setName(resultSet.getString("name"));
                        product.setPrice(resultSet.getInt("price"));
                        product.setImage(resultSet.getString("image"));
                        product.setDescription(resultSet.getString("description"));
                        product.setCreateDate(resultSet.getTimestamp("createDate"));
                        product.setStatus(resultSet.getBoolean("status"));
                        product.setActive(resultSet.getBoolean("isActive"));
                        product.setDelete(resultSet.getBoolean("isDelete"));

                        products.add(product);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> get10RecentActiveAndNotDeletedProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT TOP 10 * FROM products \n" +
                        "WHERE isActive = 1 AND isDelete = 0 AND createDate >= DATEADD(DAY, -7, GETDATE()) \n" +
                        "ORDER BY createDate DESC";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        Product product = new Product();
                        product.setProductId(resultSet.getLong("productId"));
                        product.setCategoryId(resultSet.getLong("categoryId"));
                        product.setName(resultSet.getString("name"));
                        product.setPrice(resultSet.getInt("price"));
                        product.setImage(resultSet.getString("image"));
                        product.setDescription(resultSet.getString("description"));
                        product.setCreateDate(resultSet.getTimestamp("createDate"));
                        product.setStatus(resultSet.getBoolean("status"));
                        product.setActive(resultSet.getBoolean("isActive"));
                        product.setDelete(resultSet.getBoolean("isDelete"));

                        products.add(product);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getTop10BestSellingActiveAndNotDeletedProducts() {
        List<Product> products = new ArrayList<>();

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
                        Product product = new Product();
                        product.setProductId(resultSet.getLong("productId"));
                        product.setCategoryId(resultSet.getLong("categoryId"));
                        product.setName(resultSet.getString("name"));
                        product.setPrice(resultSet.getInt("price"));
                        product.setImage(resultSet.getString("image"));
                        product.setDescription(resultSet.getString("description"));
                        product.setCreateDate(resultSet.getTimestamp("createDate"));
                        product.setStatus(resultSet.getBoolean("status"));
                        product.setActive(resultSet.getBoolean("isActive"));
                        product.setDelete(resultSet.getBoolean("isDelete"));

                        products.add(product);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> findActiveAndNotDeletedProductsByCategoryId(long categoryId) {
        List<Product> products = new ArrayList<>();

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
                            Product product = new Product();
                            product.setProductId(resultSet.getLong("productId"));
                            product.setCategoryId(resultSet.getLong("categoryId"));
                            product.setName(resultSet.getString("name"));
                            product.setPrice(resultSet.getInt("price"));
                            product.setImage(resultSet.getString("image"));
                            product.setDescription(resultSet.getString("description"));
                            product.setCreateDate(resultSet.getTimestamp("createDate"));
                            product.setStatus(resultSet.getBoolean("status"));
                            product.setActive(resultSet.getBoolean("isActive"));
                            product.setDelete(resultSet.getBoolean("isDelete"));

                            products.add(product);
                        }
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}

