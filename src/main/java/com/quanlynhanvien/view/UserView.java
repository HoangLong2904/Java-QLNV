package com.quanlynhanvien.view;

import com.quanlynhanvien.controller.LoginController;
import com.quanlynhanvien.model.Employee;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserView extends JFrame {
    private JPanel pnlCard;
    private CardLayout cardLayout;
    private String username;
    private JTextField txtMaNV, txtMaPB, txtMaCV;
    private JTextField txtHoTen, txtSDT, txtQueQuan, txtEmail;
    private JComboBox<String> cbNgay, cbThang, cbNam, cbGioiTinh;
    private JLabel lblAvatar; 
    private JButton btnEdit;    
    private JTextField txtSalaryMaNV, txtSalaryTenNV, txtSalaryMaPB, txtSalaryMaCV;
    private JTextField txtWorkDays, txtPhuCap, txtThuong, txtLuongDuKien;
    private JLabel lblSalaryAvatar;
    private JTextArea txtGhiChu;
    private int selectedYear = 2025;      
    private int selectedMonth = 1;       
    private int dayOffCount = 0;         
    private List<Integer> listSelectedDays = new ArrayList<>();
    private JPanel pnlCalendarGrid;      
    private JTextField txtYear, txtDayOff; 
    private JComboBox<Integer> cbMonth;    
    private JButton btnScheduleOk; 
    private File selectedAvatarFile = null;
    private String selectedAvatarPath = null; 
    public UserView(String username) {
        this.username = username;
        setTitle("HỆ THỐNG NHÂN VIÊN");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(245, 245, 245));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitle = new JLabel("MENU NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLUE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Log out");
        btnLogout.setBackground(new Color(255, 102, 102));
        btnLogout.setForeground(Color.WHITE);
        pnlHeader.add(btnLogout, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMenu = new JPanel(new GridLayout(10, 1, 10, 10));
        pnlMenu.setBackground(Color.WHITE);
        pnlMenu.setPreferredSize(new Dimension(200, 0));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnInfo = createMenuButton("Thông tin cá nhân");
        JButton btnSchedule = createMenuButton("Đăng ký lịch làm việc");
        JButton btnNotify = createMenuButton("Bảng lương");

        pnlMenu.add(btnInfo);
        pnlMenu.add(btnSchedule);
        pnlMenu.add(btnNotify);
        add(pnlMenu, BorderLayout.WEST);

        cardLayout = new CardLayout();
        pnlCard = new JPanel(cardLayout);

        pnlCard.add(createProfilePanel(), "PROFILE");
        pnlCard.add(createSchedulePanel(), "SCHEDULE"); 
        pnlCard.add(createSalaryPanel(), "NOTIFY"); 

        add(pnlCard, BorderLayout.CENTER);

        btnInfo.addActionListener(e -> cardLayout.show(pnlCard, "PROFILE"));
        btnSchedule.addActionListener(e -> cardLayout.show(pnlCard, "SCHEDULE"));
        btnNotify.addActionListener(e -> {
            updateSalaryData(); 
            cardLayout.show(pnlCard, "NOTIFY");
        });
        
        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                dispose();
                LoginView login = new LoginView();
                new LoginController(login);
                login.setVisible(true);
            }
        });
    }

    private JPanel createProfilePanel() {
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlIds = new JPanel(new GridBagLayout());
        pnlIds.setBackground(Color.WHITE);
        TitledBorder borderIds = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Thông tin định danh");
        pnlIds.setBorder(borderIds);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(pnlIds, gbc, 0, "Mã nhân viên:", txtMaNV = new JTextField(15));
        addFormRow(pnlIds, gbc, 1, "Mã phòng ban:", txtMaPB = new JTextField(15));
        addFormRow(pnlIds, gbc, 2, "Mã chức vụ:", txtMaCV = new JTextField(15));
        txtMaNV.setEditable(false); txtMaPB.setEditable(false); txtMaCV.setEditable(false);

        pnlMain.add(pnlIds);
        pnlMain.add(Box.createVerticalStrut(20));

        JPanel pnlDetails = new JPanel(new BorderLayout(10, 10));
        pnlDetails.setBackground(Color.WHITE);
        pnlDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(8, 5, 8, 10); gbc2.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(pnlForm, gbc2, 0, "Họ và tên:", txtHoTen = new JTextField(20));
        
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.weightx = 0;
        pnlForm.add(new JLabel("Ngày sinh:"), gbc2);
        JPanel pnlDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlDate.setBackground(Color.WHITE);
        cbNgay = new JComboBox<>(); for(int i=1;i<=31;i++) cbNgay.addItem(String.format("%02d", i));
        cbThang = new JComboBox<>(); for(int i=1;i<=12;i++) cbThang.addItem(String.format("%02d", i));
        cbNam = new JComboBox<>(); int curYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=1960;i<=curYear;i++) cbNam.addItem(String.valueOf(i)); 
        pnlDate.add(cbNgay); pnlDate.add(new JLabel(" / ")); pnlDate.add(cbThang); pnlDate.add(new JLabel(" / ")); pnlDate.add(cbNam);
        gbc2.gridx = 1; gbc2.gridy = 1; gbc2.weightx = 1.0;
        pnlForm.add(pnlDate, gbc2);

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        addFormRow(pnlForm, gbc2, 2, "Giới tính:", cbGioiTinh);
        addFormRow(pnlForm, gbc2, 3, "Số điện thoại:", txtSDT = new JTextField(20));
        addFormRow(pnlForm, gbc2, 4, "Quê quán:", txtQueQuan = new JTextField(20));
        addFormRow(pnlForm, gbc2, 5, "Email:", txtEmail = new JTextField(20));

        JPanel pnlImage = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlImage.setBackground(Color.WHITE);
        pnlImage.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 20));
        lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(120, 120));
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblAvatar.setIcon(UIManager.getIcon("FileView.computerIcon"));
        lblAvatar.setHorizontalAlignment(JLabel.CENTER);
        lblAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAvatar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { chooseImage(); }
        });
        pnlImage.add(lblAvatar);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(Color.WHITE);
        btnEdit = createStyledButton("Cập Nhật");
        pnlButtons.add(btnEdit);

        pnlDetails.add(pnlForm, BorderLayout.CENTER);
        pnlDetails.add(pnlImage, BorderLayout.EAST);
        pnlDetails.add(pnlButtons, BorderLayout.SOUTH);

        pnlMain.add(pnlDetails);
        return pnlMain;
    }

    private JPanel createSchedulePanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlTop.add(new JLabel("Năm :"), gbc);
        txtYear = new JTextField("2025", 5);
        txtYear.setHorizontalAlignment(JTextField.CENTER);
        txtYear.setEditable(false);
        gbc.gridx = 1; pnlTop.add(txtYear, gbc);

        JButton btnPrevYear = new JButton("<");
        JButton btnNextYear = new JButton(">");
        JPanel pnlYearBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pnlYearBtns.setBackground(Color.WHITE);
        pnlYearBtns.add(btnPrevYear); pnlYearBtns.add(btnNextYear);
        gbc.gridx = 2; pnlTop.add(pnlYearBtns, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlTop.add(new JLabel("Tháng :"), gbc);
        cbMonth = new JComboBox<>();
        for(int i=1; i<=12; i++) cbMonth.addItem(i);
        gbc.gridx = 1; pnlTop.add(cbMonth, gbc);

        gbc.gridx = 3; gbc.gridy = 1; pnlTop.add(new JLabel("số ngày nghỉ :"), gbc);
        txtDayOff = new JTextField("0", 3);
        txtDayOff.setHorizontalAlignment(JTextField.CENTER);
        txtDayOff.setEditable(false);
        gbc.gridx = 4; pnlTop.add(txtDayOff, gbc);

        pnl.add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCalendarBox = new JPanel(new BorderLayout());
        pnlCalendarBox.setBackground(Color.WHITE);
        pnlCalendarBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel pnlDayHeader = new JPanel(new GridLayout(1, 7));
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
        for(String d : days) {
            JLabel lbl = new JLabel(d, JLabel.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            pnlDayHeader.add(lbl);
        }
        pnlCalendarBox.add(pnlDayHeader, BorderLayout.NORTH);

        pnlCalendarGrid = new JPanel(new GridLayout(0, 7, 1, 1));
        pnlCalendarGrid.setBackground(Color.LIGHT_GRAY);
        renderCalendar(); 
        pnlCalendarBox.add(pnlCalendarGrid, BorderLayout.CENTER);

        pnl.add(pnlCalendarBox, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        pnlBottom.setBackground(Color.WHITE);
        JButton btnCancel = new JButton("CANCEL");
        btnScheduleOk = new JButton("OK"); 
        btnCancel.setContentAreaFilled(false); btnCancel.setBorderPainted(false); btnCancel.setForeground(Color.BLUE);
        btnScheduleOk.setContentAreaFilled(false); btnScheduleOk.setBorderPainted(false); btnScheduleOk.setForeground(Color.BLUE);
        pnlBottom.add(btnCancel); pnlBottom.add(btnScheduleOk);
        pnl.add(pnlBottom, BorderLayout.SOUTH);

        btnNextYear.addActionListener(e -> { selectedYear++; txtYear.setText(String.valueOf(selectedYear)); renderCalendar(); });
        btnPrevYear.addActionListener(e -> { selectedYear--; txtYear.setText(String.valueOf(selectedYear)); renderCalendar(); });
        cbMonth.addActionListener(e -> { selectedMonth = (int)cbMonth.getSelectedItem(); renderCalendar(); });

        btnCancel.addActionListener(e -> {
            listSelectedDays.clear();
            dayOffCount = 0;
            txtDayOff.setText("0");
            renderCalendar(); 
        });

        return pnl;
    }

    private JPanel createSalaryPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblCompany = new JLabel("Công ty xyz");
        lblCompany.setFont(new Font("Arial", Font.ITALIC, 14));
        pnlMain.add(lblCompany, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlContent.add(new JLabel("Mã nhân viên :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtSalaryMaNV = new JTextField(12), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; pnlContent.add(new JLabel("Tên nhân viên :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtSalaryTenNV = new JTextField(12), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; pnlContent.add(new JLabel("Mã phòng ban :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtSalaryMaPB = new JTextField(12), gbc);

        lblSalaryAvatar = new JLabel();
        lblSalaryAvatar.setPreferredSize(new Dimension(100, 100));
        lblSalaryAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblSalaryAvatar.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 2;
        pnlContent.add(lblSalaryAvatar, gbc);

        gbc.gridheight = 1; 
        gbc.gridx = 2; gbc.gridy = 2; pnlContent.add(new JLabel("Mã chức vụ :"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; pnlContent.add(txtSalaryMaCV = new JTextField(10), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        pnlContent.add(new JSeparator(), gbc);

        gbc.gridwidth = 1; gbc.gridy = 4; gbc.gridx = 0; pnlContent.add(new JLabel("Số ngày đi làm :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtWorkDays = new JTextField(10), gbc);

        gbc.gridy = 5; gbc.gridx = 0; pnlContent.add(new JLabel("Phụ cấp :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtPhuCap = new JTextField("500,000"), gbc);

        gbc.gridy = 6; gbc.gridx = 0; pnlContent.add(new JLabel("Thưởng :"), gbc);
        gbc.gridx = 1; pnlContent.add(txtThuong = new JTextField("1,000,000"), gbc);

        gbc.gridx = 2; gbc.gridy = 4; pnlContent.add(new JLabel("Ghi chú :"), gbc);
        txtGhiChu = new JTextArea(3, 10);
        txtGhiChu.setText("A comment");
        txtGhiChu.setBackground(new Color(255, 204, 51));
        txtGhiChu.setLineWrap(true);
        JScrollPane sp = new JScrollPane(txtGhiChu);
        gbc.gridx = 3; gbc.gridy = 4; gbc.gridheight = 3; gbc.fill = GridBagConstraints.BOTH;
        pnlContent.add(sp, gbc);

        gbc.gridheight = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel pnlFinal = new JPanel(new FlowLayout());
        pnlFinal.setBackground(Color.WHITE);
        pnlFinal.add(new JLabel("Lương dự kiến :"));
        pnlFinal.add(txtLuongDuKien = new JTextField(15));
        pnlContent.add(pnlFinal, gbc);

        JTextField[] fields = {txtSalaryMaNV, txtSalaryTenNV, txtSalaryMaPB, txtSalaryMaCV, 
                              txtWorkDays, txtPhuCap, txtThuong, txtLuongDuKien};
        for(JTextField f : fields) f.setEditable(false);
        txtGhiChu.setEditable(false);

        pnlMain.add(pnlContent, BorderLayout.CENTER);
        return pnlMain;
    }

    private void updateSalaryData() {
        txtSalaryMaNV.setText(txtMaNV.getText());
        txtSalaryTenNV.setText(txtHoTen.getText());
        txtSalaryMaPB.setText(txtMaPB.getText());
        txtSalaryMaCV.setText(txtMaCV.getText());
        lblSalaryAvatar.setIcon(lblAvatar.getIcon());

        Calendar cal = Calendar.getInstance();
        cal.set(selectedYear, selectedMonth - 1, 1);
        int totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int actualWorkDays = totalDays - dayOffCount;
        txtWorkDays.setText(String.valueOf(actualWorkDays));

        try {
            long pc = Long.parseLong(txtPhuCap.getText().replace(",", ""));
            long th = Long.parseLong(txtThuong.getText().replace(",", ""));
            long total = (actualWorkDays * 500000) + pc + th;
            txtLuongDuKien.setText(String.format("%,d VNĐ", total));
        } catch(Exception ex) {}
    }

    private void renderCalendar() {
        pnlCalendarGrid.removeAll(); 
        Calendar cal = Calendar.getInstance();
        cal.set(selectedYear, selectedMonth - 1, 1); 
        int startDay = cal.get(Calendar.DAY_OF_WEEK); 
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH); 

        for (int i = 1; i < startDay; i++) {
            pnlCalendarGrid.add(new JLabel("")); 
        }

        for (int i = 1; i <= maxDays; i++) {
            final int day = i;
            JButton btnDay = new JButton(String.valueOf(i));
            if (listSelectedDays.contains(day)) {
                btnDay.setBackground(Color.RED);
                btnDay.setForeground(Color.WHITE);
            } else {
                btnDay.setBackground(Color.WHITE);
                btnDay.setForeground(Color.BLACK);
            }

            btnDay.setFocusPainted(false);
            btnDay.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            btnDay.addActionListener(e -> {
                if (btnDay.getBackground() == Color.WHITE) {
                    btnDay.setBackground(Color.RED);   
                    btnDay.setForeground(Color.WHITE); 
                    listSelectedDays.add(day);
                } else {
                    btnDay.setBackground(Color.WHITE); 
                    btnDay.setForeground(Color.BLACK); 
                    listSelectedDays.remove(Integer.valueOf(day));
                }
                dayOffCount = listSelectedDays.size();
                txtDayOff.setText(String.valueOf(dayOffCount));
                updateSalaryData();
            });
            pnlCalendarGrid.add(btnDay);
        }
        pnlCalendarGrid.revalidate();
        pnlCalendarGrid.repaint();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedAvatarFile = fileChooser.getSelectedFile();
            selectedAvatarPath = selectedAvatarFile.getAbsolutePath(); 
            displayImage(selectedAvatarPath);
        }
    }

    private void displayImage(String path) {
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(path)
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
                lblAvatar.setIcon(icon);
                lblSalaryAvatar.setIcon(icon);
            }
        }
    }

    public void setDayOffCount(int count) {
        this.dayOffCount = count;
        this.txtDayOff.setText(String.valueOf(count));
        updateSalaryData();
    }

    public void addEditListener(ActionListener al) { btnEdit.addActionListener(al); }

    public void setUserInfo(Employee emp) {
        txtMaNV.setText(emp.getMaNV()); txtMaPB.setText(emp.getMaPB()); txtMaCV.setText(emp.getMaCV());
        txtHoTen.setText(emp.getHoTen()); txtSDT.setText(emp.getSdt());
        txtQueQuan.setText(emp.getQueQuan()); txtEmail.setText(emp.getEmail());
        cbGioiTinh.setSelectedItem(emp.getGioiTinh());
        if (emp.getHinhAnh() != null) {
            this.selectedAvatarPath = emp.getHinhAnh();
            displayImage(selectedAvatarPath);
        }

        if(emp.getNgaySinh() != null) {
            String[] parts = emp.getNgaySinh().toString().split("-");
            cbNam.setSelectedItem(parts[0]); cbThang.setSelectedItem(parts[1]); cbNgay.setSelectedItem(parts[2]);
        }
        updateSalaryData();
    }

    public Employee getEmployeeFromForm() {
        Employee e = new Employee();
        e.setMaNV(txtMaNV.getText()); e.setHoTen(txtHoTen.getText()); e.setSdt(txtSDT.getText());
        e.setQueQuan(txtQueQuan.getText()); e.setEmail(txtEmail.getText());
        e.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        e.setHinhAnh(this.selectedAvatarPath); 
        String dateStr = cbNam.getSelectedItem() + "-" + cbThang.getSelectedItem() + "-" + cbNgay.getSelectedItem();
        e.setNgaySinh(java.sql.Date.valueOf(dateStr));
        return e;
    }

    public int getDayOffCount() { return dayOffCount; }

    public double getCalculatedSalary() {
        try {
            String s = txtLuongDuKien.getText().replaceAll("[^0-9]", "");
            return s.isEmpty() ? 0 : Double.parseDouble(s);
        } catch (Exception e) { return 0; }
    }

    public int getSelectedMonth() { return selectedMonth; }
    public int getSelectedYear() { return selectedYear; }

    public void addScheduleOkListener(ActionListener al) {
        if(btnScheduleOk != null) btnScheduleOk.addActionListener(al);
    }

    public void setDayOffCountFromDB(int count) {
        this.dayOffCount = count;
        this.txtDayOff.setText(String.valueOf(count));
        updateSalaryData();
    }
    public void setSavedDays(List<Integer> days) {
        this.listSelectedDays.clear();
        if(days != null) this.listSelectedDays.addAll(days);
        renderCalendar();
    }

    public List<Integer> getSelectedDaysList() {
        return new ArrayList<>(listSelectedDays);
    }

    public void addMonthYearChangeListener(ActionListener al) {
        cbMonth.addActionListener(al);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text); btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false); return btn;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text); btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false); return btn;
    }
}
