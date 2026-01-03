package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.DepartmentDAO;
import com.quanlynhanvien.model.Department;
import com.quanlynhanvien.view.DepartmentPanel;
import javax.swing.JOptionPane;

public class DepartmentController {
    private DepartmentPanel view;
    private DepartmentDAO dao;

    public DepartmentController(DepartmentPanel view) {
        this.view = view;
        this.dao = new DepartmentDAO();
        
        loadData();
        
        this.view.addAddListener(e -> addDept());
        this.view.addEditListener(e -> updateDept());
        this.view.addDeleteListener(e -> deleteDept());
    }
    
    private void loadData() {
        view.showList(dao.getAllDepartments());
    }
    
    private void addDept() {
        Department d = view.getDepartmentFromForm();
        if (d.getMaPB().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã phòng không được để trống!");
            return;
        }
        if (dao.addDepartment(d)) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại (Trùng mã?)");
        }
    }
    
    private void updateDept() {
        Department d = view.getDepartmentFromForm();
        if (dao.updateDepartment(d)) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
        }
    }
    
    private void deleteDept() {
        Department d = view.getDepartmentFromForm();
        if (JOptionPane.showConfirmDialog(view, "Bạn chắc chắn muốn xóa?") == JOptionPane.YES_OPTION) {
            if (dao.deleteDepartment(d.getMaPB())) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}