package com.quanlynhanvien.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    
    // 1. Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE Username = ? AND Password = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy tài khoản
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Tạo tài khoản mới (CẬP NHẬT QUAN TRỌNG: Thêm tham số maNV)
    public boolean createAccount(String user, String pass, String role, String maNV) {
        String sql = "INSERT INTO TaiKhoan (Username, Password, Role, MaNV) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role); // Lưu quyền (0: Admin, 1: User)
            ps.setString(4, maNV); // Lưu Mã nhân viên để liên kết dữ liệu
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // e.printStackTrace(); // Bật lên nếu muốn xem lỗi chi tiết (ví dụ trùng mã)
            return false;
        }
    }

    // 3. Xóa tài khoản
    public boolean deleteAccount(String username) {
        String sql = "DELETE FROM TaiKhoan WHERE Username = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 4. Lấy quyền hạn (Role) của tài khoản
    public String getUserRole(String username) {
        String role = "";
        String sql = "SELECT Role FROM TaiKhoan WHERE Username = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("Role");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    // 5. Lấy Mã Nhân Viên từ Tên đăng nhập (Dùng để load thông tin User)
    public String getMaNVByUsername(String username) {
        String maNV = "";
        String sql = "SELECT MaNV FROM TaiKhoan WHERE Username = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maNV = rs.getString("MaNV");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maNV;
    }
    
    // 6. Đổi mật khẩu (Hàm phụ trợ, có thể dùng sau này)
    public boolean changePassword(String username, String newPass) {
        String sql = "UPDATE TaiKhoan SET Password = ? WHERE Username = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPass);
            ps.setString(2, username);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}