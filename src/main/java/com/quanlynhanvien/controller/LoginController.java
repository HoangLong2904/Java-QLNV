package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.AccountDAO;
import com.quanlynhanvien.view.LoginView;
import com.quanlynhanvien.view.MainView;
import com.quanlynhanvien.view.UserView;
import javax.swing.*;

public class LoginController {
    private LoginView loginView;
    private AccountDAO accountDAO;

    public LoginController(LoginView view) {
        this.loginView = view;
        this.accountDAO = new AccountDAO();
        this.loginView.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        try {
            String user = loginView.getUsername();
            String pass = loginView.getPassword();

            if (user.isEmpty() || pass.isEmpty()) {
                loginView.showMessage("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
                return;
            }
            
            if (accountDAO.checkLogin(user, pass)) {
                String role = accountDAO.getUserRole(user);
                String maNV = accountDAO.getMaNVByUsername(user); 

                loginView.dispose();
                if ("0".equals(role) || "Admin".equalsIgnoreCase(role)) {
                    openAdminScreen(role);
                } else {
                    if (maNV != null && !maNV.isEmpty()) {
                        openUserScreen(maNV); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Lỗi: Tài khoản này chưa được gán Mã Nhân Viên!");
                    }
                }
            } else {
                loginView.showMessage("Tài khoản hoặc mật khẩu không chính xác!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "Lỗi hệ thống: " + e.getMessage());
        }
    }
    private void openAdminScreen(String role) {
        try {
            MainView mainView = new MainView(role);

            new EmployeeController(mainView.getEmployeePanel(), role);
            new DepartmentController(mainView.getDepartmentPanel());
            new PositionController(mainView.getPositionPanel());
            new StatisticsController(mainView.getStatisticsPanel());
            
            mainView.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khởi tạo màn hình Admin: " + e.getMessage());
        }
    }
    private void openUserScreen(String maNV) {
        try {
            UserView userView = new UserView(maNV); 
            new UserController(userView, maNV); 
            userView.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khởi tạo màn hình User: " + e.getMessage());
        }
    }
}