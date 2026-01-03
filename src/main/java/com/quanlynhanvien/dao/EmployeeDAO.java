package com.quanlynhanvien.dao;

import com.quanlynhanvien.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 1. Lấy danh sách nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT nv.*, cv.TenCV, pb.TenPB FROM NhanVien nv " +
                     "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                     "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee emp = mappingEmployee(rs);
                list.add(emp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Cập nhật thông tin cá nhân (ĐÃ THÊM CỘT HINHANH)
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE NhanVien SET HoTen=?, NgaySinh=?, GioiTinh=?, SDT=?, QueQuan=?, Email=?, HinhAnh=? WHERE MaNV=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getHoTen());
            ps.setDate(2, emp.getNgaySinh());
            ps.setString(3, emp.getGioiTinh());
            ps.setString(4, emp.getSdt());
            ps.setString(5, emp.getQueQuan());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getHinhAnh()); // Lưu đường dẫn ảnh
            ps.setString(8, emp.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 3. Lấy nhân viên theo ID
    public Employee getEmployeeById(String maNV) {
        String sql = "SELECT nv.*, cv.TenCV, pb.TenPB FROM NhanVien nv " +
                     "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                     "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB WHERE nv.MaNV = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mappingEmployee(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 4. LƯU HOẶC CẬP NHẬT BẢNG LƯƠNG
    public boolean saveOrUpdateSalary(String maNV, int thang, int nam, int soNgayNghi, double luongDuKien) {
        String sql = "INSERT INTO BangLuong (MaNV, Thang, Nam, SoNgayNghi, LuongDuKien) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE SoNgayNghi = VALUES(SoNgayNghi), LuongDuKien = VALUES(LuongDuKien)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ps.setInt(4, soNgayNghi);
            ps.setDouble(5, luongDuKien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. LẤY LƯƠNG ĐÃ LƯU
    public Object[] getSavedSalary(String maNV, int thang, int nam) {
        String sql = "SELECT SoNgayNghi, LuongDuKien FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{ rs.getInt("SoNgayNghi"), rs.getDouble("LuongDuKien") };
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // --- MỚI: LƯU CHI TIẾT CÁC Ô ĐỎ (CÁC NGÀY NGHỈ CỤ THỂ) ---
    public boolean saveDayOffDetails(String maNV, int thang, int nam, List<Integer> days) {
        String deleteSql = "DELETE FROM ChiTietNgayNghi WHERE MaNV=? AND Thang=? AND Nam=?";
        String insertSql = "INSERT INTO ChiTietNgayNghi (MaNV, Ngay, Thang, Nam) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection()) {
            conn.setAutoCommit(false);
            // Xóa dữ liệu cũ của tháng đó để chèn mới hoàn toàn
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setString(1, maNV); psDel.setInt(2, thang); psDel.setInt(3, nam);
                psDel.executeUpdate();
            }
            // Chèn danh sách các ngày được bôi đỏ
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (int d : days) {
                    psIns.setString(1, maNV); psIns.setInt(2, d);
                    psIns.setInt(3, thang); psIns.setInt(4, nam);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }
            conn.commit();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // --- MỚI: LẤY DANH SÁCH CÁC NGÀY NGHỈ ĐỂ BÔI ĐỎ LẠI LỊCH ---
    public List<Integer> getDayOffDetails(String maNV, int thang, int nam) {
        List<Integer> days = new ArrayList<>();
        String sql = "SELECT Ngay FROM ChiTietNgayNghi WHERE MaNV=? AND Thang=? AND Nam=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV); ps.setInt(2, thang); ps.setInt(3, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    days.add(rs.getInt("Ngay"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return days;
    }

    private Employee mappingEmployee(ResultSet rs) throws Exception {
        Employee emp = new Employee();
        emp.setMaNV(rs.getString("MaNV"));
        emp.setHoTen(rs.getString("HoTen"));
        emp.setNgaySinh(rs.getDate("NgaySinh"));
        emp.setGioiTinh(rs.getString("GioiTinh"));
        emp.setSdt(rs.getString("SDT"));
        emp.setQueQuan(rs.getString("QueQuan"));
        emp.setEmail(rs.getString("Email"));
        emp.setHinhAnh(rs.getString("HinhAnh")); // Lấy đường dẫn ảnh từ DB
        emp.setMaCV(rs.getString("MaCV"));
        emp.setMaPB(rs.getString("MaPB"));
        emp.setTenChucVu(rs.getString("TenCV"));
        emp.setTenPhongBan(rs.getString("TenPB"));
        return emp;
    }
}