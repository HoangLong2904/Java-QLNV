package com.quanlynhanvien.model;

public class ComboBoxItem {
    private String id;   // Mã (Ví dụ: PB01) - Dùng để lưu DB
    private String name; // Tên (Ví dụ: Phòng Kế Toán) - Dùng để hiển thị

    public ComboBoxItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    // Hàm này quyết định cái gì sẽ hiện lên trên ComboBox
    @Override
    public String toString() {
        return name; 
    }
    
    // Hàm hỗ trợ so sánh để chọn đúng dòng khi click bảng
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComboBoxItem) {
            ComboBoxItem other = (ComboBoxItem) obj;
            return other.name.equals(this.name); // So sánh theo tên hiển thị trên bảng
        }
        return false;
    }
}