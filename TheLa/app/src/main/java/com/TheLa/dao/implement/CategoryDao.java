package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.dao.ICategoryDao;
import com.TheLa.models.CategoryModel;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements ICategoryDao {
    @Override
    public List<CategoryModel> getAllActiveAndNotDeletedCategories() {
        List<CategoryModel> categories = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM categories WHERE isActive = 1 AND isDelete = 0";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        CategoryModel category = new CategoryModel();
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

