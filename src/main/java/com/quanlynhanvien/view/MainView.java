package com.quanlynhanvien.view;

import com.quanlynhanvien.controller.LoginController; 
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JPanel pnlCard; 
    private CardLayout cardLayout;
    
    // Các màn hình con (View)
    private EmployeeView employeePanel;
    private DepartmentPanel departmentPanel;
    private PositionPanel positionPanel; // MỚI THÊM: Panel Chức vụ

    public MainView(String role) {
        setTitle("HỆ THỐNG QUẢN TRỊ");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(245, 245, 245)); 
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblHeader = new JLabel("HỆ THỐNG QUẢN TRỊ", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        lblHeader.setForeground(Color.BLUE);
        pnlHeader.add(lblHeader, BorderLayout.CENTER);
        
        JButton btnLogout = new JButton("Log out");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(new Color(255, 102, 102));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        pnlHeader.add(btnLogout, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. SIDEBAR (MENU BÊN TRÁI) ---
        JPanel pnlMenu = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlMenu.setBackground(new Color(230, 230, 230));
        pnlMenu.setPreferredSize(new Dimension(220, 0));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton btnNhanVien = createMenuButton("Quản lý nhân viên");
        JButton btnPhongBan = createMenuButton("Quản lý phòng ban");
        JButton btnChucVu = createMenuButton("Quản lý chức vụ");
        JButton btnTaiKhoan = createMenuButton("Cấp quyền tài khoản");
        JButton btnThongKe = createMenuButton("Báo cáo thống kê");

        pnlMenu.add(btnNhanVien);
        pnlMenu.add(btnPhongBan);
        pnlMenu.add(btnChucVu);
        pnlMenu.add(btnTaiKhoan);
        pnlMenu.add(btnThongKe);

        add(pnlMenu, BorderLayout.WEST);

        // --- 3. MAIN CONTENT (NỘI DUNG CHÍNH) ---
        cardLayout = new CardLayout();
        pnlCard = new JPanel(cardLayout);
        pnlCard.setBackground(Color.WHITE);

        // Khởi tạo các Panel con
        employeePanel = new EmployeeView();
        employeePanel.setRole(role); 
        
        departmentPanel = new DepartmentPanel();
        
        positionPanel = new PositionPanel(); // MỚI THÊM: Khởi tạo Panel Chức vụ
        
        // Thêm vào CardLayout
        pnlCard.add(employeePanel, "NHANVIEN");
        pnlCard.add(departmentPanel, "PHONGBAN");
        pnlCard.add(positionPanel, "CHUCVU"); // MỚI THÊM: Gắn Panel vào CardLayout
        pnlCard.add(new JLabel("Chức năng Tài Khoản đang phát triển...", JLabel.CENTER), "TAIKHOAN");

        add(pnlCard, BorderLayout.CENTER);

        // --- XỬ LÝ SỰ KIỆN CHUYỂN TAB ---
        btnNhanVien.addActionListener(e -> cardLayout.show(pnlCard, "NHANVIEN"));
        btnPhongBan.addActionListener(e -> cardLayout.show(pnlCard, "PHONGBAN"));
        btnChucVu.addActionListener(e -> cardLayout.show(pnlCard, "CHUCVU")); // Chuyển sang tab Chức vụ
        btnTaiKhoan.addActionListener(e -> cardLayout.show(pnlCard, "TAIKHOAN"));
        
        // Sự kiện Đăng xuất
        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                LoginView loginView = new LoginView();
                new LoginController(loginView); 
                loginView.setVisible(true);
            }
        });
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(new Color(225, 225, 225));
        btn.setFocusPainted(false);
        return btn;
    }
    
    // --- GETTERS ĐỂ CONTROLLER GỌI ---
    public EmployeeView getEmployeePanel() { return employeePanel; }
    
    public DepartmentPanel getDepartmentPanel() { return departmentPanel; }
    
    // MỚI THÊM: Getter cho PositionPanel
    public PositionPanel getPositionPanel() { return positionPanel; }
}