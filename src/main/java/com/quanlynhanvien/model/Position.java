package com.quanlynhanvien.model;

public class Position {
    private String maCV;
    private String tenCV;
    private double luongCoBan;
    private String ghiChu;

    public Position() { }

    public Position(String maCV, String tenCV, double luongCoBan, String ghiChu) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.luongCoBan = luongCoBan;
        this.ghiChu = ghiChu;
    }

    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }
    public String getTenCV() { return tenCV; }
    public void setTenCV(String tenCV) { this.tenCV = tenCV; }
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}