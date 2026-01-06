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
    private String hinhAnh;
    private String maCV;      
    private String maPB;     
    private String tenChucVu; 
    private String tenPhongBan;

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

    // Constructor đầy đủ (Hiển thị lên bảng)
    public Employee(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String sdt, String queQuan, String email, String maCV, String maPB, String tenChucVu, String tenPhongBan) {
        this(maNV, hoTen, ngaySinh, gioiTinh, sdt, queQuan, email, maCV, maPB);
        this.tenChucVu = tenChucVu;
        this.tenPhongBan = tenPhongBan;
    }

    // --- GETTERS & SETTERS (PHẢI CÓ ĐỦ ĐỂ CONTROLLER GỌI ĐƯỢC) ---
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
    
    // Getter cho Ảnh
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    // Getter cho Mã CV và Mã PB (Sửa lỗi đỏ ở Controller)
    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    // Getter cho Tên hiển thị
    public String getTenChucVu() { return tenChucVu; }
    public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }

    public String getTenPhongBan() { return tenPhongBan; }
    public void setTenPhongBan(String tenPhongBan) { this.tenPhongBan = tenPhongBan; }
}