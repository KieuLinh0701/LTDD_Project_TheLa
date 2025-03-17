package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.ISizeDao;
import com.TheLa.models.SizeModel;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SizeDao implements ISizeDao {
    @Override
    public SizeModel findSizeBySizeId(Long sizeId) {
        SizeModel model = new SizeModel();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM sizes WHERE sizeId = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setLong(1, sizeId);

                    // Thực hiện truy vấn và xử lý kết quả
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {

                            model.setSizeId(resultSet.getLong("sizeId"));
                            model.setName(resultSet.getString("name"));
                            model.setDescription(resultSet.getString("description"));
                        }
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
}
