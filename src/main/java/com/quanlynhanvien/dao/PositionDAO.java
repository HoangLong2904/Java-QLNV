package com.quanlynhanvien.dao;

import com.quanlynhanvien.model.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PositionDAO {
    
    public List<Position> getAllPositions() {
        List<Position> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Position(
                    rs.getString("MaCV"),
                    rs.getString("TenCV"),
                    rs.getDouble("LuongCoBan"),
                    rs.getString("GhiChu")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addPosition(Position p) {
        String sql = "INSERT INTO ChucVu (MaCV, TenCV, LuongCoBan, GhiChu) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMaCV());
            ps.setString(2, p.getTenCV());
            ps.setDouble(3, p.getLuongCoBan());
            ps.setString(4, p.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean updatePosition(Position p) {
        String sql = "UPDATE ChucVu SET TenCV=?, LuongCoBan=?, GhiChu=? WHERE MaCV=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTenCV());
            ps.setDouble(2, p.getLuongCoBan());
            ps.setString(3, p.getGhiChu());
            ps.setString(4, p.getMaCV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean deletePosition(String maCV) {
        String sql = "DELETE FROM ChucVu WHERE MaCV=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // Kiểm tra xem chức vụ có đang được sử dụng bởi nhân viên nào không
    public boolean hasEmployees(String maCV) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaCV = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}