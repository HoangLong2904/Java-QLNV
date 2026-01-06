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

        loadUserInfo();

        loadDataToView();

        this.view.addScheduleOkListener(e -> handleSaveSalary());

        this.view.addMonthYearChangeListener(e -> loadDataToView());

        this.view.addEditListener(e -> handleUpdateInfo());
    }
    private void loadUserInfo() {
        try {
            Employee emp = dao.getEmployeeById(username);
            if (emp != null) {
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

        List<Integer> savedDays = dao.getDayOffDetails(username, thang, nam);
        view.setSavedDays(savedDays);

        Object[] data = dao.getSavedSalary(username, thang, nam);
        
        if (data != null) {
            int soNgayNghi = (int) data[0];
            view.setDayOffCountFromDB(soNgayNghi);
        } else {
            view.setDayOffCountFromDB(0);
        }
    }

    private void handleUpdateInfo() {
        try {
            Employee emp = view.getEmployeeFromForm();
            emp.setMaNV(this.username);

            int confirm = JOptionPane.showConfirmDialog(view, 
                    "Bạn có chắc chắn muốn cập nhật thông tin cá nhân không?", 
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadUserInfo(); 
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

        List<Integer> selectedDays = view.getSelectedDaysList();
        boolean saveSalary = dao.saveOrUpdateSalary(username, thang, nam, nghi, luong);
        boolean saveDays = dao.saveDayOffDetails(username, thang, nam, selectedDays);

        if (saveSalary && saveDays) {
            JOptionPane.showMessageDialog(view, "Đã lưu lịch làm việc và các ngày nghỉ tháng " + thang + "/" + nam);
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu!");
        }
    }
}