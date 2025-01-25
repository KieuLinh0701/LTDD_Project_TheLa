package com.TheLa.repository;

import android.util.Log;

import com.TheLa.models.User;
import com.TheLa.sqlServer.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository {
//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        Connection connection = null;
//
//        try {
//            connection = DatabaseHelper.connectToDatabase();
//            String sql = "SELECT * FROM users";
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                String email = resultSet.getString("email");
//                users.add(new User(id, name, email));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            DatabaseHelper.closeConnection(connection);
//        }
//
//        return users;
//    }

    public void addUser(User user) {
        try (Connection connection = DatabaseHelper.connectToDatabase()) {
            if (connection != null) {
                String sql = "INSERT INTO users (name, email, password, code, address, phone, role, isDelete, isActivate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getEmail());
                    preparedStatement.setString(3, user.getPassword());
                    preparedStatement.setString(4, user.getCode()); // code
                    preparedStatement.setString(5, user.getAddress()); // address
                    preparedStatement.setString(6, user.getPhone()); // phone
                    preparedStatement.setString(7, user.getRole());
                    preparedStatement.setBoolean(8, user.getDelete() != null ? user.getDelete() : false);
                    preparedStatement.setBoolean(9, user.getActive() != null ? user.getActive() : false);

                    int rowsAffected = preparedStatement.executeUpdate();
                    Log.i("Database", "Rows affected: " + rowsAffected);
                } catch (SQLException e) {
                    Log.e("Database", "SQL Error: " + e.getMessage(), e);
                }
            } else {
                Log.w("Database", "Connection is null");
            }
        } catch (SQLException e) {
            Log.e("Database", "Error connecting to database", e);
        }
    }



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
                                    resultSet.getBoolean("isDelete"),
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

