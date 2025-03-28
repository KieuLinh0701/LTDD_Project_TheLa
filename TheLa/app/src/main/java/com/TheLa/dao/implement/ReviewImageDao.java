package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.IProductImageDao;
import com.TheLa.dao.IReviewImageDao;
import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ReviewImageModel;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewImageDao implements IReviewImageDao {
    @Override
    public List<ReviewImageModel> findReviewImageByReviewId(Long reviewId) {
        List<ReviewImageModel> list = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM reviewImages WHERE reviewId = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setLong(1, reviewId);

                    // Thực hiện truy vấn và xử lý kết quả
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            ReviewImageModel model = new ReviewImageModel();
                            model.setImageId(resultSet.getLong("imageId"));
                            model.setReviewId(resultSet.getLong("reviewId"));
                            model.setImage(resultSet.getString("image"));

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
