package com.TheLa.repository.implement;

import android.util.Log;

import com.TheLa.models.Category;
import com.TheLa.models.User;
import com.TheLa.repository.ICategoryRepository;
import com.TheLa.repository.IUserRepository;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    @Override
    public List<Category> getAllActiveAndNotDeletedCategories() {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM categories WHERE isActive = 1 AND isDelete = 0";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        Category category = new Category();
                        category.setCategoryId(resultSet.getLong("categoryId"));
                        category.setName(resultSet.getString("name"));
                        category.setImage(resultSet.getString("image"));
                        category.setActive(resultSet.getBoolean("isActive"));
                        category.setDelete(resultSet.getBoolean("isDelete"));

                        categories.add(category);
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}

