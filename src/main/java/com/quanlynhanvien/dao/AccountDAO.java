package com.quanlynhanvien.dao;

import com.quanlynhanvien.model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    
    // Hàm kiểm tra đăng nhập: Trả về true nếu đúng, false nếu sai
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE Username = ? AND Password = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            // Nếu có kết quả trả về (next() == true) nghĩa là đăng nhập đúng
            return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Thêm vào trong class AccountDAO
public boolean deleteAccount(String username) {
    String sql = "DELETE FROM TaiKhoan WHERE Username = ?";
    try (java.sql.Connection conn = ConnectDB.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        return false;
    }
}
    
    public String getUserRole(String username) {
    String role = "";
    String sql = "SELECT Role FROM TaiKhoan WHERE Username = ?";
    try (Connection conn = ConnectDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            role = rs.getString("Role");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return role;
    }
    // Thêm hàm này vào file AccountDAO.java
    public boolean createAccount(String user, String pass, String role) {
        // Lưu ý: Tạm thời chúng ta để MaNV là NULL cho đơn giản hóa chức năng đăng ký nhanh
        String sql = "INSERT INTO TaiKhoan (Username, Password, Role) VALUES (?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // e.printStackTrace(); // Bật cái này nếu muốn xem lỗi chi tiết
            return false; // Trả về false nếu trùng username hoặc lỗi khác
        }
    }
public String getMaNVByUsername(String username) {
    String maNV = "";
    String sql = "SELECT MaNV FROM TaiKhoan WHERE Username = ?";
    try (java.sql.Connection conn = com.quanlynhanvien.dao.ConnectDB.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            maNV = rs.getString("MaNV");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return maNV;
}
}