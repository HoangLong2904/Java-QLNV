package com.quanlynhanvien.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Chuỗi kết nối cho MySQL (XAMPP mặc định cổng 3306)
            String url = "jdbc:mysql://localhost:3306/QuanLyNhanSu?useSSL=false&characterEncoding=UTF-8"; 
            String user = "root"; // Mặc định XAMPP là root
            String password = ""; // Mặc định XAMPP không có pass

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}