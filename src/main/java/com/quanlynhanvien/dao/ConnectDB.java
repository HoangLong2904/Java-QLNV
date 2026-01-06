package com.quanlynhanvien.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/QuanLyNhanSu?useSSL=false&characterEncoding=UTF-8"; 
            String user = "root"; 
            String password = ""; 

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}