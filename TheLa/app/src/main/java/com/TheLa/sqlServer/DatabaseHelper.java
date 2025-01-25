package com.TheLa.sqlServer;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static String ip = "192.168.1.7";
    private static String port = "1433";
    private static String classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "DBTheLa";
    private static String username = "test";
    private static String password = "123456";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;
    private static Connection connection;


    // Phương thức kết nối đến cơ sở dữ liệu SQL Server
    public static Connection connectToDatabase() {
        connection = null;
        try {
            Class.forName(classes);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection; // Trả về kết nối
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close(); // Đóng kết nối
                Log.w("Connection", "closed");
            } catch (SQLException e) {
                Log.w("Error closing connection", "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

