package com.TheLa.dao.implement;

import android.util.Log;

import com.TheLa.models.User;
import com.TheLa.dao.IUserDao;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao implements IUserDao {
    @Override
    public User addUser(User user) {
        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "INSERT INTO users (name, email, password, code, address, phone, role, image, isActivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getCode());
                preparedStatement.setString(5, user.getAddress());
                preparedStatement.setString(6, user.getPhone());
                preparedStatement.setString(7, user.getRole());
                preparedStatement.setString(8, user.getImage());
                preparedStatement.setBoolean(9, user.getActive() != null ? user.getActive() : false);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Long userId = generatedKeys.getLong(1);
                            user.setUserId(userId);
                            return user;
                        }
                    }
                }
                Log.w("Database", "Failed to add user, no rows affected");
            } else {
                Log.w("Database", "Connection is null");
            }
        } catch (SQLException e) {
            Log.e("Database", "Error adding user to database", e);
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "UPDATE users SET name = ?, email = ?, password = ?, code = ?, address = ?, phone = ?, role = ?, image = ?, isActivate = ? WHERE userId = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getCode());
                preparedStatement.setString(5, user.getAddress());
                preparedStatement.setString(6, user.getPhone());
                preparedStatement.setString(7, user.getRole());
                preparedStatement.setString(8, user.getImage());
                preparedStatement.setBoolean(9, user.getActive() != null ? user.getActive() : false);
                preparedStatement.setLong(10, user.getUserId());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected>0) {
                    Log.w("Ket noi", "thanh cong");
                }
                else {
                    Log.w("Ket noi", "that bai");
                }
                return rowsAffected > 0;
            } else {
                Log.w("Database", "Connection is null");
            }
        } catch (SQLException e) {
            Log.e("Database", "Error connecting to database", e);
        }
        return false;
    }

    @Override
    public User getUserFindByEmail(String email) {
        User user = null;

        // Sử dụng try-with-resources để tự động đóng tài nguyên
        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "SELECT * FROM users WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, email);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        // Lấy thông tin người dùng nếu tồn tại
                        if (resultSet.next()) {
                            user = new User(
                                    resultSet.getLong("userId"),
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("password"),
                                    resultSet.getString("code"),
                                    resultSet.getString("address"),
                                    resultSet.getString("phone"),
                                    resultSet.getString("role"),
                                    resultSet.getString("image"),
                                    resultSet.getBoolean("isActivate")
                            );
                        }
                    }
                }
            } else {
                Log.w("Connection", "Connection is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}

