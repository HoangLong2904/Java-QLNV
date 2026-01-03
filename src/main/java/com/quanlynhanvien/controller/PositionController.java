package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.PositionDAO;
import com.quanlynhanvien.model.Position;
import com.quanlynhanvien.view.PositionPanel;
import javax.swing.JOptionPane;

public class PositionController {
    private PositionPanel view;
    private PositionDAO dao;

    public PositionController(PositionPanel view) {
        this.view = view;
        this.dao = new PositionDAO();
        
        loadData();
        
        this.view.addAddListener(e -> addPos());
        this.view.addEditListener(e -> updatePos());
        this.view.addDeleteListener(e -> deletePos());
    }
    
    private void loadData() {
        view.showList(dao.getAllPositions());
    }
    
    private void addPos() {
        Position p = view.getPositionFromForm();
        if (p.getMaCV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã chức vụ không được để trống!");
            return;
        }
        if (dao.addPosition(p)) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại (Trùng mã?)");
        }
    }
    
    private void updatePos() {
        Position p = view.getPositionFromForm();
        if (dao.updatePosition(p)) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
        }
    }
    
    private void deletePos() {
        Position p = view.getPositionFromForm();
        if (p.getMaCV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn chức vụ cần xóa!");
            return;
        }
        
        // Kiểm tra ràng buộc dữ liệu
        if (dao.hasEmployees(p.getMaCV())) {
            JOptionPane.showMessageDialog(view, "Không thể xóa: Đang có nhân viên giữ chức vụ này!");
            return;
        }

        if (JOptionPane.showConfirmDialog(view, "Bạn chắc chắn muốn xóa?") == JOptionPane.YES_OPTION) {
            if (dao.deletePosition(p.getMaCV())) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}