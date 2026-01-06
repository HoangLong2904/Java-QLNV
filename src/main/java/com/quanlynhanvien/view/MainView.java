package com.quanlynhanvien.view;

import com.quanlynhanvien.controller.LoginController; 
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JPanel pnlCard; 
    private CardLayout cardLayout;
    private EmployeeView employeePanel;
    private DepartmentPanel departmentPanel;
    private PositionPanel positionPanel;
    private StatisticsPanel statisticsPanel; 

    public MainView(String role) {
        setTitle("HỆ THỐNG QUẢN TRỊ");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        JPanel pnlMenu = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlMenu.setBackground(new Color(230, 230, 230));
        pnlMenu.setPreferredSize(new Dimension(220, 0));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton btnNhanVien = createMenuButton("Quản lý nhân viên");
        JButton btnPhongBan = createMenuButton("Quản lý phòng ban");
        JButton btnChucVu = createMenuButton("Quản lý chức vụ");
        JButton btnThongKe = createMenuButton("Báo cáo thống kê");

        pnlMenu.add(btnNhanVien);
        pnlMenu.add(btnPhongBan);
        pnlMenu.add(btnChucVu);
        pnlMenu.add(btnThongKe);

        add(pnlMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        pnlCard = new JPanel(cardLayout);
        pnlCard.setBackground(Color.WHITE);

        employeePanel = new EmployeeView();
        employeePanel.setRole(role); 
        
        departmentPanel = new DepartmentPanel();
        positionPanel = new PositionPanel(); 
        
        statisticsPanel = new StatisticsPanel(); 
    
        pnlCard.add(employeePanel, "NHANVIEN");
        pnlCard.add(departmentPanel, "PHONGBAN");
        pnlCard.add(positionPanel, "CHUCVU");
        pnlCard.add(statisticsPanel, "THONGKE");        

        add(pnlCard, BorderLayout.CENTER);

        btnNhanVien.addActionListener(e -> cardLayout.show(pnlCard, "NHANVIEN"));
        btnPhongBan.addActionListener(e -> cardLayout.show(pnlCard, "PHONGBAN"));
        btnChucVu.addActionListener(e -> cardLayout.show(pnlCard, "CHUCVU"));
        btnThongKe.addActionListener(e -> cardLayout.show(pnlCard, "THONGKE"));

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
    public EmployeeView getEmployeePanel() { return employeePanel; }   
    public DepartmentPanel getDepartmentPanel() { return departmentPanel; }
    public PositionPanel getPositionPanel() { return positionPanel; }
    public StatisticsPanel getStatisticsPanel() { return statisticsPanel; }
}