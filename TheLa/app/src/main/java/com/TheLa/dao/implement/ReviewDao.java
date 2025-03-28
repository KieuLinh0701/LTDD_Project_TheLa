package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.ICategoryDao;
import com.TheLa.dao.IReviewDao;
import com.TheLa.models.CategoryModel;
import com.TheLa.models.ReviewModel;
import com.TheLa.models.UserModel;
import com.TheLa.services.implement.UserService;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao implements IReviewDao {
    UserService userService = new UserService();
    @Override
    public List<ReviewModel> findReviewByProductId(Long productId) {
        List<ReviewModel> list = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM reviews WHERE productId = ? ORDER BY reviewDate DESC;";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    // Gán giá trị cho tham số productId
                    preparedStatement.setLong(1, productId);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            ReviewModel model = new ReviewModel();
                            model.setReviewId(resultSet.getLong("reviewId"));
                            model.setProductId(resultSet.getLong("productId"));
                            model.setRating(resultSet.getInt("rating"));
                            model.setContent(resultSet.getString("content"));
                            model.setReviewDate(resultSet.getTimestamp("reviewDate"));

                            Long userId = resultSet.getLong("userId");
                            UserModel user = userService.getUserFindByUserId(userId);
                            model.setUser(user);

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

