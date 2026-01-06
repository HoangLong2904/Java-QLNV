package com.quanlynhanvien.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {
    public boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE Username = ? AND Password = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean createAccount(String user, String pass, String role, String maNV) {
        String sql = "INSERT INTO TaiKhoan (Username, Password, Role, MaNV) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role); 
            ps.setString(4, maNV); 
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
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