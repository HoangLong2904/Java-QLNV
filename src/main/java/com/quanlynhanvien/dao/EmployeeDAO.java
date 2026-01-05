package com.quanlynhanvien.dao;

import com.quanlynhanvien.model.Employee;
import com.quanlynhanvien.model.SalaryStatistic; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 1. Lấy danh sách nhân viên (KẾT NỐI 3 BẢNG để lấy Tên CV và Tên PB)
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT nv.*, cv.TenCV, pb.TenPB FROM NhanVien nv " +
                     "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                     "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mappingEmployee(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm nhân viên mới (Đầy đủ các trường bao gồm MaCV, MaPB, HinhAnh)
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO NhanVien(MaNV, HoTen, NgaySinh, GioiTinh, SDT, QueQuan, Email, MaCV, MaPB, HinhAnh) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, emp.getMaNV());
            ps.setString(2, emp.getHoTen());
            ps.setDate(3, emp.getNgaySinh());
            ps.setString(4, emp.getGioiTinh());
            ps.setString(5, emp.getSdt());
            ps.setString(6, emp.getQueQuan());
            ps.setString(7, emp.getEmail());
            ps.setString(8, emp.getMaCV());     
            ps.setString(9, emp.getMaPB());     
            ps.setString(10, emp.getHinhAnh()); 
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin nhân viên
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE NhanVien SET HoTen=?, NgaySinh=?, GioiTinh=?, SDT=?, QueQuan=?, Email=?, MaCV=?, MaPB=?, HinhAnh=? WHERE MaNV=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, emp.getHoTen());
            ps.setDate(2, emp.getNgaySinh());
            ps.setString(3, emp.getGioiTinh());
            ps.setString(4, emp.getSdt());
            ps.setString(5, emp.getQueQuan());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getMaCV());     
            ps.setString(8, emp.getMaPB());     
            ps.setString(9, emp.getHinhAnh());  
            ps.setString(10, emp.getMaNV());    
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa nhân viên
    public boolean deleteEmployee(String maNV) {
        // Lưu ý: Nếu Database chưa set CASCADE, bạn cần xóa dữ liệu ở các bảng con (BangLuong, TaiKhoan...) trước
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lấy thông tin chi tiết 1 nhân viên theo Mã (Dùng cho UserView)
    public Employee getEmployeeById(String maNV) {
        String sql = "SELECT nv.*, cv.TenCV, pb.TenPB FROM NhanVien nv " +
                     "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                     "LEFT JOIN PhongBan pb ON nv.MaPB = pb.MaPB WHERE nv.MaNV = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mappingEmployee(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 6. Lưu hoặc Cập nhật bảng lương tổng quát (Số ngày nghỉ, Lương dự kiến)
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

    // 7. Lấy thông tin lương đã lưu
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

    // 8. Lưu chi tiết các ngày nghỉ (ô đỏ trên lịch)
    public boolean saveDayOffDetails(String maNV, int thang, int nam, List<Integer> days) {
        String deleteSql = "DELETE FROM ChiTietNgayNghi WHERE MaNV=? AND Thang=? AND Nam=?";
        String insertSql = "INSERT INTO ChiTietNgayNghi (MaNV, Ngay, Thang, Nam) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu Transaction
            
            // Bước 1: Xóa dữ liệu cũ của tháng đó
            try (PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
                psDel.setString(1, maNV); 
                psDel.setInt(2, thang); 
                psDel.setInt(3, nam);
                psDel.executeUpdate();
            }
            
            // Bước 2: Chèn danh sách các ngày mới
            try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                for (int d : days) {
                    psIns.setString(1, maNV); 
                    psIns.setInt(2, d);
                    psIns.setInt(3, thang); 
                    psIns.setInt(4, nam);
                    psIns.addBatch(); // Gom lệnh lại chạy 1 lần
                }
                psIns.executeBatch();
            }
            conn.commit(); // Xác nhận Transaction
            return true;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 9. Lấy danh sách các ngày nghỉ (để tô đỏ lại lịch)
    public List<Integer> getDayOffDetails(String maNV, int thang, int nam) {
        List<Integer> days = new ArrayList<>();
        String sql = "SELECT Ngay FROM ChiTietNgayNghi WHERE MaNV=? AND Thang=? AND Nam=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV); 
            ps.setInt(2, thang); 
            ps.setInt(3, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    days.add(rs.getInt("Ngay"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return days;
    }

    // 10. Lấy danh sách thống kê lương chi tiết (Dùng cho Báo cáo Thống kê)
    public List<SalaryStatistic> getSalaryStatistics(int thang, int nam) {
        List<SalaryStatistic> list = new ArrayList<>();
        
        // LEFT JOIN BangLuong: Để nhân viên chưa lưu lương vẫn hiện ra (với giá trị 0)
        String sql = "SELECT nv.MaNV, nv.HoTen, pb.TenPB, cv.TenCV, cv.LuongCoBan, " +
                     "COALESCE(bl.SoNgayNghi, 0) as SoNgayNghi, " +
                     "COALESCE(bl.LuongDuKien, 0) as LuongThucLinh " +
                     "FROM NhanVien nv " +
                     "JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                     "JOIN PhongBan pb ON nv.MaPB = pb.MaPB " +
                     "LEFT JOIN BangLuong bl ON nv.MaNV = bl.MaNV AND bl.Thang = ? AND bl.Nam = ?";
                     
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SalaryStatistic(
                    rs.getString("MaNV"),
                    rs.getString("HoTen"),
                    rs.getString("TenPB"),
                    rs.getString("TenCV"),
                    rs.getDouble("LuongCoBan"),
                    rs.getInt("SoNgayNghi"),
                    rs.getDouble("LuongThucLinh")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- HÀM HỖ TRỢ MAP DỮ LIỆU ---
    private Employee mappingEmployee(ResultSet rs) throws Exception {
        Employee emp = new Employee();
        emp.setMaNV(rs.getString("MaNV"));
        emp.setHoTen(rs.getString("HoTen"));
        emp.setNgaySinh(rs.getDate("NgaySinh"));
        emp.setGioiTinh(rs.getString("GioiTinh"));
        emp.setSdt(rs.getString("SDT"));
        emp.setQueQuan(rs.getString("QueQuan"));
        emp.setEmail(rs.getString("Email"));
        
        // Map các trường mới
        emp.setHinhAnh(rs.getString("HinhAnh")); // Ảnh
        emp.setMaCV(rs.getString("MaCV"));       // Mã CV
        emp.setMaPB(rs.getString("MaPB"));       // Mã PB
        
        // Map tên hiển thị (Chỉ có khi join bảng)
        try { emp.setTenChucVu(rs.getString("TenCV")); } catch (Exception e) {}
        try { emp.setTenPhongBan(rs.getString("TenPB")); } catch (Exception e) {}
        
        return emp;
    }
}