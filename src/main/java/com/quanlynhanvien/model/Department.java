package com.quanlynhanvien.model;

public class Department {
    private String maPB;
    private String tenPB;
    private String diaChi;
    private String sdt;
    private String ghiChu;

    public Department() { }

    public Department(String maPB, String tenPB, String diaChi, String sdt, String ghiChu) {
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.ghiChu = ghiChu;
    }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }
    public String getTenPB() { return tenPB; }
    public void setTenPB(String tenPB) { this.tenPB = tenPB; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}