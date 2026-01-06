package com.quanlynhanvien.model;

public class ComboBoxItem {
    private String id; 
    private String name; 

    public ComboBoxItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name; 
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComboBoxItem) {
            ComboBoxItem other = (ComboBoxItem) obj;
            return other.name.equals(this.name); 
        }
        return false;
    }
}