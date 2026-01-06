package com.quanlynhanvien.model;

public class SalaryStatistic {
    private String maNV;
    private String hoTen;
    private String phongBan;
    private String chucVu;
    private double luongCoBan;
    private int soNgayNghi;
    private double tongLuong; 

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

    public String getMaNV() { return maNV; }
    public String getHoTen() { return hoTen; }
    public String getPhongBan() { return phongBan; }
    public String getChucVu() { return chucVu; }
    public double getLuongCoBan() { return luongCoBan; }
    public int getSoNgayNghi() { return soNgayNghi; }
    public double getTongLuong() { return tongLuong; }
    
    public int getSoNgayDiLam() {
        int workDays = 30 - soNgayNghi;
        return workDays < 0 ? 0 : workDays;
    }
}