package com.quanlynhanvien.view;

import com.quanlynhanvien.model.ComboBoxItem;
import com.quanlynhanvien.model.Department;
import com.quanlynhanvien.model.Employee;
import com.quanlynhanvien.model.Position;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EmployeeView extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField txtMaNV, txtHoTen, txtSDT, txtQueQuan, txtEmail;
    
    // Ngày sinh
    private JComboBox<String> cbNgay, cbThang, cbNam;
    private JComboBox<String> cbGioiTinh;
    
    // THAY ĐỔI LỚN: Dùng ComboBoxItem
    private JComboBox<ComboBoxItem> cbChucVu;
    private JComboBox<ComboBoxItem> cbPhongBan;
    
    private JButton btnAdd, btnEdit, btnDelete, btnClear, btnExport;

    public EmployeeView() {
        setLayout(new BorderLayout(10, 10)); 
        setBackground(Color.WHITE); 

        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS)); 
        pnlTop.setBackground(Color.WHITE);
        
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN SỰ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        pnlTitle.add(lblTitle);
        pnlTop.add(pnlTitle);

        initInputForm(pnlTop);
        add(pnlTop, BorderLayout.NORTH);
        initTable();
    }

    private void initInputForm(JPanel pnlContainer) {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- CỘT 1 ---
        // Mã NV
        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(new JLabel("Mã Nhân Viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; 
        txtMaNV = new JTextField(20); pnlForm.add(txtMaNV, gbc);

        // Họ Tên
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(new JLabel("Họ và Tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; 
        txtHoTen = new JTextField(20); pnlForm.add(txtHoTen, gbc);

        // Ngày Sinh
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(new JLabel("Ngày Sinh:"), gbc);
        JPanel pnlDate = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlDate.setBackground(Color.WHITE);
        cbNgay = new JComboBox<>(); cbThang = new JComboBox<>(); cbNam = new JComboBox<>();
        for (int i = 1; i <= 31; i++) cbNgay.addItem(String.format("%02d", i));
        for (int i = 1; i <= 12; i++) cbThang.addItem(String.format("%02d", i));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1960; i <= currentYear; i++) cbNam.addItem(String.valueOf(i));
        cbNam.setSelectedItem("2000");
        pnlDate.add(cbNgay); pnlDate.add(new JLabel("/")); pnlDate.add(cbThang); pnlDate.add(new JLabel("/")); pnlDate.add(cbNam);
        gbc.gridx = 1; gbc.gridy = 2; pnlForm.add(pnlDate, gbc);

        // Giới Tính
        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; 
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"}); cbGioiTinh.setBackground(Color.WHITE);
        pnlForm.add(cbGioiTinh, gbc);

        // --- CỘT 2 --- (Thêm các trường mới vào đây)
        // SĐT
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; pnlForm.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; 
        txtSDT = new JTextField(20); pnlForm.add(txtSDT, gbc);

        // Quê Quán
        gbc.gridx = 2; gbc.gridy = 1; pnlForm.add(new JLabel("Quê Quán:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; 
        txtQueQuan = new JTextField(20); pnlForm.add(txtQueQuan, gbc);

        // Email
        gbc.gridx = 2; gbc.gridy = 2; pnlForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; 
        txtEmail = new JTextField(20); pnlForm.add(txtEmail, gbc);
        
        // --- CHỨC VỤ & PHÒNG BAN (MỚI) ---
        gbc.gridx = 2; gbc.gridy = 3; pnlForm.add(new JLabel("Chức Vụ:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3;
        cbChucVu = new JComboBox<>(); cbChucVu.setBackground(Color.WHITE);
        pnlForm.add(cbChucVu, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4; pnlForm.add(new JLabel("Phòng Ban:"), gbc);
        gbc.gridx = 3; gbc.gridy = 4;
        cbPhongBan = new JComboBox<>(); cbPhongBan.setBackground(Color.WHITE);
        pnlForm.add(cbPhongBan, gbc);

        // --- NÚT BẤM ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(Color.WHITE);
        
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        btnExport = new JButton("Xuất Excel");
        
        btnAdd.setBackground(new Color(0, 153, 76)); btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(255, 128, 0)); btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(204, 0, 0)); btnDelete.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(0, 102, 204)); btnClear.setForeground(Color.WHITE);
        btnExport.setBackground(new Color(0, 153, 153)); btnExport.setForeground(Color.WHITE); 

        pnlButtons.add(btnAdd); pnlButtons.add(btnEdit); pnlButtons.add(btnDelete);
        pnlButtons.add(btnClear); pnlButtons.add(btnExport); 

        pnlContainer.add(pnlForm);
        pnlContainer.add(pnlButtons);
    }

    private void initTable() {
        String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Quê Quán", "Email", "Chức Vụ", "Phòng Ban"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 240, 255));
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(table.getValueAt(row, 0).toString());
                    txtHoTen.setText(table.getValueAt(row, 1).toString());
                    try {
                        String dateStr = table.getValueAt(row, 2).toString();
                        String[] parts = dateStr.split("-");
                        if (parts.length == 3) {
                            cbNam.setSelectedItem(parts[0]);
                            cbThang.setSelectedItem(parts[1]);
                            cbNgay.setSelectedItem(parts[2]);
                        }
                    } catch (Exception ex) {}

                    cbGioiTinh.setSelectedItem(table.getValueAt(row, 3).toString());
                    txtSDT.setText(table.getValueAt(row, 4).toString());
                    txtQueQuan.setText(table.getValueAt(row, 5).toString());
                    txtEmail.setText(table.getValueAt(row, 6).toString());
                    
                    // --- CHỌN LẠI COMBOBOX KHI CLICK ---
                    String tenChucVuTrenBang = table.getValueAt(row, 7).toString();
                    String tenPhongBanTrenBang = table.getValueAt(row, 8).toString();
                    
                    selectComboBoxItemByName(cbChucVu, tenChucVuTrenBang);
                    selectComboBoxItemByName(cbPhongBan, tenPhongBanTrenBang);
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    // Hàm hỗ trợ chọn ComboBox theo tên hiển thị
    private void selectComboBoxItemByName(JComboBox<ComboBoxItem> cb, String name) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).getName().equals(name)) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    // --- HÀM CẬP NHẬT DỮ LIỆU CHO COMBOBOX (Controller sẽ gọi hàm này) ---
    public void updateComboBoxData(List<Department> listDep, List<Position> listPos) {
        cbPhongBan.removeAllItems();
        for (Department d : listDep) {
            cbPhongBan.addItem(new ComboBoxItem(d.getMaPB(), d.getTenPB()));
        }
        
        cbChucVu.removeAllItems();
        for (Position p : listPos) {
            cbChucVu.addItem(new ComboBoxItem(p.getMaCV(), p.getTenCV()));
        }
    }

    public Employee getEmployeeFromForm() {
        try {
            String ma = txtMaNV.getText();
            String ten = txtHoTen.getText();
            String y = cbNam.getSelectedItem().toString();
            String m = cbThang.getSelectedItem().toString();
            String d = cbNgay.getSelectedItem().toString();
            Date ngaysinh = Date.valueOf(y + "-" + m + "-" + d);
            String gt = cbGioiTinh.getSelectedItem().toString();
            String sdt = txtSDT.getText();
            String que = txtQueQuan.getText();
            String email = txtEmail.getText();
            
            // LẤY MÃ TỪ COMBOBOX
            ComboBoxItem selectedPos = (ComboBoxItem) cbChucVu.getSelectedItem();
            ComboBoxItem selectedDep = (ComboBoxItem) cbPhongBan.getSelectedItem();
            
            String maCV = selectedPos != null ? selectedPos.getId() : "";
            String maPB = selectedDep != null ? selectedDep.getId() : "";
            
            return new Employee(ma, ten, ngaysinh, gt, sdt, que, email, maCV, maPB);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            return null;
        }
    }
    
    public void showListEmployees(List<Employee> list) {
        tableModel.setRowCount(0);
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                emp.getMaNV(), emp.getHoTen(), emp.getNgaySinh(), emp.getGioiTinh(),
                emp.getSdt(), emp.getQueQuan(), emp.getEmail(),
                emp.getTenChucVu(), emp.getTenPhongBan()
            });
        }
    }
    
    // ... (Giữ nguyên các hàm setRole và addListener như cũ)
    public void setRole(String role) {
        boolean isAdmin = "Admin".equalsIgnoreCase(role);
        btnAdd.setEnabled(isAdmin); btnEdit.setEnabled(isAdmin); btnDelete.setEnabled(isAdmin);
        txtMaNV.setEditable(isAdmin); txtHoTen.setEditable(isAdmin);
        cbNgay.setEnabled(isAdmin); cbThang.setEnabled(isAdmin); cbNam.setEnabled(isAdmin);
        cbGioiTinh.setEnabled(isAdmin);
        txtSDT.setEditable(isAdmin); txtQueQuan.setEditable(isAdmin); txtEmail.setEditable(isAdmin);
        cbChucVu.setEnabled(isAdmin); cbPhongBan.setEnabled(isAdmin);
    }
    public void addAddListener(ActionListener al) { btnAdd.addActionListener(al); }
    public void addEditListener(ActionListener al) { btnEdit.addActionListener(al); }
    public void addDeleteListener(ActionListener al) { btnDelete.addActionListener(al); }
    public void addExportListener(ActionListener al) { btnExport.addActionListener(al); }
    public void addClearListener(ActionListener al) { 
        btnClear.addActionListener(e -> {
            txtMaNV.setText(""); txtHoTen.setText(""); 
            cbNam.setSelectedIndex(0); cbThang.setSelectedIndex(0); cbNgay.setSelectedIndex(0);
            txtSDT.setText(""); txtQueQuan.setText(""); txtEmail.setText("");
            table.clearSelection(); 
        }); 
    }
}