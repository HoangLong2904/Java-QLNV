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
        // 1. Lấy thông tin từ giao diện
        String user = loginView.getUsername();
        String pass = loginView.getPassword();

        if (user.isEmpty() || pass.isEmpty()) {
            loginView.showMessage("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }
        
        // 2. Kiểm tra đăng nhập qua AccountDAO
        if (accountDAO.checkLogin(user, pass)) {
            // Lấy quyền (Role) và Mã nhân viên (MaNV) liên kết với tài khoản này
            String role = accountDAO.getUserRole(user);
            String maNV = accountDAO.getMaNVByUsername(user); // Hàm này bạn đã thêm ở Bước 2

            // 3. Đóng cửa sổ đăng nhập
            loginView.dispose();

            // 4. Kiểm tra quyền để mở màn hình tương ứng
            if ("Admin".equalsIgnoreCase(role)) {
                // Nếu là Admin -> Mở MainView (Màn hình quản trị tổng thể)
                openAdminScreen(role);
            } else {
                // Nếu là User -> Mở UserView (Màn hình cá nhân)
                // Phải có MaNV thì mới lấy được thông tin nhân viên để hiển thị
                if (maNV != null && !maNV.isEmpty()) {
                    openUserScreen(maNV); 
                } else {
                    JOptionPane.showMessageDialog(null, "Lỗi: Tài khoản này chưa được gán Mã Nhân Viên!");
                    // Nếu lỗi có thể cho quay lại màn hình login hoặc xử lý tùy ý
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
    
    // Tách hàm để dễ quản lý lỗi
    private void openAdminScreen(String role) {
    MainView mainView = new MainView(role); // Mở MainView cho Admin
    // Khởi tạo các controller cho các tab của Admin
    new EmployeeController(mainView.getEmployeePanel(), role);
    new DepartmentController(mainView.getDepartmentPanel());
    new PositionController(mainView.getPositionPanel());
    mainView.setVisible(true);
}

  private void openUserScreen(String maNV) {
    try {
        // Khởi tạo giao diện cá nhân
        UserView userView = new UserView(maNV); 
        // Khởi tạo Controller điều khiển dữ liệu cho giao diện đó
        new UserController(userView, maNV); 
        userView.setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}