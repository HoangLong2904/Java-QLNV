package com.quanlynhanvien.model;

import java.sql.Date;

public class Employee {
    private String maNV;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String sdt;
    private String queQuan;
    private String email;
    
    // Các trường mới để xử lý ComboBox
    private String maCV;      // Mã chức vụ (Lưu DB)
    private String maPB;      // Mã phòng ban (Lưu DB)
    private String tenChucVu; // Tên hiển thị (Lên bảng)
    private String tenPhongBan;// Tên hiển thị (Lên bảng)
    
    // MỚI: Trường lưu đường dẫn ảnh
    private String hinhAnh;

    public Employee() {
    }

    // Constructor dùng cho việc THÊM MỚI (Lấy từ Form xuống)
    public Employee(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String sdt, String queQuan, String email, String maCV, String maPB) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.queQuan = queQuan;
        this.email = email;
        this.maCV = maCV;
        this.maPB = maPB;
    }

    // Constructor đầy đủ (Dùng khi lấy từ DB lên để hiển thị)
    public Employee(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String sdt, String queQuan, String email, String maCV, String maPB, String tenChucVu, String tenPhongBan) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.queQuan = queQuan;
        this.email = email;
        this.maCV = maCV;
        this.maPB = maPB;
        this.tenChucVu = tenChucVu;
        this.tenPhongBan = tenPhongBan;
    }

    // --- GETTERS AND SETTERS ---
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Getter/Setter cho MÃ (Quan trọng để sửa lỗi đỏ)
    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    // Getter/Setter cho TÊN
    public String getTenChucVu() { return tenChucVu; }
    public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }

    public String getTenPhongBan() { return tenPhongBan; }
    public void setTenPhongBan(String tenPhongBan) { this.tenPhongBan = tenPhongBan; }

    // MỚI: Getter và Setter cho hinhAnh
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}