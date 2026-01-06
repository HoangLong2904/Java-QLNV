package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.EmployeeDAO;
import com.quanlynhanvien.model.Employee;
import com.quanlynhanvien.view.UserView;
import javax.swing.JOptionPane;
import java.util.List;

public class UserController {
    private UserView view;
    private EmployeeDAO dao;
    private String username; 

    public UserController(UserView view, String username) {
        this.view = view;
        this.username = username;
        this.dao = new EmployeeDAO();
        
        // 1. Load thông tin cá nhân ngay khi đăng nhập (Bao gồm cả ảnh)
        loadUserInfo();
        
        // 2. Load dữ liệu lương/nghỉ của tháng hiện tại (bao gồm cả ô đỏ)
        loadDataToView();

        // Lắng nghe sự kiện nút "OK" ở tab Lịch để lưu lương và ô đỏ
        this.view.addScheduleOkListener(e -> handleSaveSalary());

        // Lắng nghe sự kiện thay đổi Tháng/Năm để load lại dữ liệu tương ứng
        this.view.addMonthYearChangeListener(e -> loadDataToView());

        // Gán sự kiện cho nút "Sửa" bằng Lambda
        this.view.addEditListener(e -> handleUpdateInfo());
    }

    /**
     * Lấy thông tin chi tiết của nhân viên từ Database dựa trên mã nhân viên
     */
    private void loadUserInfo() {
        try {
            Employee emp = dao.getEmployeeById(username);
            if (emp != null) {
                // View nhận Object Employee có chứa đường dẫn HinhAnh và tự render
                view.setUserInfo(emp); 
            } else {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin cho mã: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }


    private void loadDataToView() {
        int thang = view.getSelectedMonth();
        int nam = view.getSelectedYear();
        
        // 1. Tải danh sách các ngày đã bôi đỏ để hiển thị lại trên lịch
        List<Integer> savedDays = dao.getDayOffDetails(username, thang, nam);
        view.setSavedDays(savedDays);
        
        // 2. Tải thông tin lương tổng quát
        Object[] data = dao.getSavedSalary(username, thang, nam);
        
        if (data != null) {
            int soNgayNghi = (int) data[0];
            // Cập nhật số ngày nghỉ lên View (View sẽ tự tính lại lương hiển thị)
            view.setDayOffCountFromDB(soNgayNghi);
        } else {
            // Nếu tháng này chưa có dữ liệu trong DB, reset về 0
            view.setDayOffCountFromDB(0);
        }
    }

    private void handleUpdateInfo() {
        try {
            // Lấy dữ liệu từ Form (Lúc này view.getEmployeeFromForm() phải lấy được path ảnh)
            Employee emp = view.getEmployeeFromForm();
            emp.setMaNV(this.username);

            int confirm = JOptionPane.showConfirmDialog(view, 
                    "Bạn có chắc chắn muốn cập nhật thông tin cá nhân không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // DAO.updateEmployee lúc này phải thực hiện update cả cột HinhAnh
                if (dao.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadUserInfo(); // Load lại để đồng bộ ảnh lên các Label
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, "Ngày sinh không hợp lệ! Vui lòng kiểm tra lại.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Đã xảy ra lỗi: " + ex.getMessage());
        }
    }

    private void handleSaveSalary() {
        int thang = view.getSelectedMonth(); 
        int nam = view.getSelectedYear(); 
        int nghi = view.getDayOffCount();
        double luong = view.getCalculatedSalary();
        
        // Lấy danh sách chi tiết các ngày đã bôi đỏ từ View
        List<Integer> selectedDays = view.getSelectedDaysList();

        // Bước 1: Lưu bảng lương tổng quát
        boolean saveSalary = dao.saveOrUpdateSalary(username, thang, nam, nghi, luong);
        
        // Bước 2: Lưu chi tiết từng ngày nghỉ (ô đỏ)
        boolean saveDays = dao.saveDayOffDetails(username, thang, nam, selectedDays);

        if (saveSalary && saveDays) {
            JOptionPane.showMessageDialog(view, "Đã lưu lịch làm việc và các ngày nghỉ tháng " + thang + "/" + nam);
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu!");
        }
    }
}