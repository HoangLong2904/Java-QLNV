package com.quanlynhanvien.model;

public class SalaryStatistic {
    private String maNV;
    private String hoTen;
    private String phongBan;
    private String chucVu;
    private double luongCoBan;
    private int soNgayNghi;
    private double tongLuong; // Lấy từ LuongDuKien trong DB

    public SalaryStatistic() { }

    public SalaryStatistic(String maNV, String hoTen, String phongBan, String chucVu, double luongCoBan, int soNgayNghi, double tongLuong) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.phongBan = phongBan;
        this.chucVu = chucVu;
        this.luongCoBan = luongCoBan;
        this.soNgayNghi = soNgayNghi;
        this.tongLuong = tongLuong;
    }

    // Getters
    public String getMaNV() { return maNV; }
    public String getHoTen() { return hoTen; }
    public String getPhongBan() { return phongBan; }
    public String getChucVu() { return chucVu; }
    public double getLuongCoBan() { return luongCoBan; }
    public int getSoNgayNghi() { return soNgayNghi; }
    public double getTongLuong() { return tongLuong; }
    
    // Tính số ngày đi làm (Giả sử tháng chuẩn 26 công)
    public int getSoNgayDiLam() {
        int workDays = 26 - soNgayNghi;
        return workDays < 0 ? 0 : workDays;
    }
}