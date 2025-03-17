package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.IProductSizeDao;
import com.TheLa.models.ProductModel;
import com.TheLa.models.ProductSizeModel;
import com.TheLa.models.SizeModel;
import com.TheLa.services.implement.SizeService;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductSizeDao implements IProductSizeDao {

    private final SizeService sizeService = new SizeService();

    @Override
    public List<ProductSizeModel> findProductSizeByProduct(ProductModel product) {
        List<ProductSizeModel> list = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM productSizes WHERE productId = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setLong(1, product.getProductId());

                    // Thực hiện truy vấn và xử lý kết quả
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            ProductSizeModel model = new ProductSizeModel();

                            model.setProductSizeId(resultSet.getLong("productSizeId"));
                            model.setPrice(resultSet.getBigDecimal("price"));

                            model.setProduct(product);

                            Long sizeId = (resultSet.getLong("sizeId"));
                            model.setSize(sizeService.findSizeBySizeId(sizeId));

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
