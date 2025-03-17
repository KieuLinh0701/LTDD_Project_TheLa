package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.IProductImageDao;
import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ProductModel;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductImageDao implements IProductImageDao {
    @Override
    public List<ProductImageModel> findProductImageByProduct(Long productId) {
        List<ProductImageModel> list = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM productImages WHERE productId = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setLong(1, productId);

                    // Thực hiện truy vấn và xử lý kết quả
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            ProductImageModel model = new ProductImageModel();
                            model.setImageId(resultSet.getLong("imageId"));
                            model.setProductId(resultSet.getLong("productId"));
                            model.setImage(resultSet.getString("image"));
                            model.setMain(resultSet.getBoolean("isMain"));

                            list.add(model);
                        }
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
