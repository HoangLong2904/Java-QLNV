package com.quanlynhanvien.main;

import com.quanlynhanvien.controller.LoginController;
import com.quanlynhanvien.view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Khởi chạy màn hình Đăng Nhập trước
            LoginView view = new LoginView();
            new LoginController(view);
            view.setVisible(true);
        });
    }
}