package com.quanlynhanvien.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    // Đã xóa lblRegister (Nút đăng ký)

    public LoginView() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblHeader = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setForeground(Color.BLUE);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // Form
        JPanel pnlForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("Tài khoản:"), gbc); // Sửa label cho rõ nghĩa

        gbc.gridx = 1; gbc.gridy = 0;
        txtUsername = new JTextField(15);
        pnlForm.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtPassword = new JPasswordField(15);
        pnlForm.add(txtPassword, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Button Panel
        JPanel pnlButton = new JPanel(new FlowLayout());
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(120, 35));
        pnlButton.add(btnLogin);
        pnlButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(pnlButton, BorderLayout.SOUTH);
    }

    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void addLoginListener(ActionListener al) { btnLogin.addActionListener(al); }
    
    // Hàm này giữ lại nhưng để trống hoặc xóa đi bên Controller cũng được
    public void addRegisterListener(MouseAdapter ma) { 
        // Không làm gì cả vì đã xóa nút đăng ký
    }
}