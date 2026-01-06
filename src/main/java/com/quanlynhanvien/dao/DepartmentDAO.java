package com.quanlynhanvien.dao;

import com.quanlynhanvien.model.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Department d = new Department();
                d.setMaPB(rs.getString("MaPB"));
                d.setTenPB(rs.getString("TenPB"));
                d.setDiaChi(rs.getString("DiaChi"));
                d.setSdt(rs.getString("SDT"));
                d.setGhiChu(rs.getString("GhiChu"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addDepartment(Department d) {
        String sql = "INSERT INTO PhongBan (MaPB, TenPB, DiaChi, SDT, GhiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getMaPB());
            ps.setString(2, d.getTenPB());
            ps.setString(3, d.getDiaChi());
            ps.setString(4, d.getSdt());
            ps.setString(5, d.getGhiChu());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDepartment(Department d) {
        String sql = "UPDATE PhongBan SET TenPB=?, DiaChi=?, SDT=?, GhiChu=? WHERE MaPB=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getTenPB());
            ps.setString(2, d.getDiaChi());
            ps.setString(3, d.getSdt());
            ps.setString(4, d.getGhiChu());
            ps.setString(5, d.getMaPB());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDepartment(String maPB) {
        String sql = "DELETE FROM PhongBan WHERE MaPB=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPB);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasEmployees(String maPB) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE MaPB = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPB);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}