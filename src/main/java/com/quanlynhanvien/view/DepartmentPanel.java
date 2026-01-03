package com.quanlynhanvien.view;

import com.quanlynhanvien.model.Department;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DepartmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaPB, txtTenPB, txtDiaChi, txtSDT;
    private JTextArea txtGhiChu;
    private JButton btnAdd, btnEdit, btnDelete;

    public DepartmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // FORM
        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin phòng ban"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlTop.add(new JLabel("Mã phòng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtMaPB = new JTextField(15); pnlTop.add(txtMaPB, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlTop.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtSDT = new JTextField(15); pnlTop.add(txtSDT, gbc);

        gbc.gridx = 2; gbc.gridy = 0; pnlTop.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; txtTenPB = new JTextField(15); pnlTop.add(txtTenPB, gbc);

        gbc.gridx = 2; gbc.gridy = 1; pnlTop.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; txtDiaChi = new JTextField(15); pnlTop.add(txtDiaChi, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlTop.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; 
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlTop.add(txtGhiChu, gbc);

        add(pnlTop, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"Mã PB", "Tên Phòng", "Địa Chỉ", "SĐT", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        
        // Sự kiện click bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaPB.setText(table.getValueAt(row, 0).toString());
                    txtTenPB.setText(table.getValueAt(row, 1).toString());
                    txtDiaChi.setText(table.getValueAt(row, 2).toString());
                    txtSDT.setText(table.getValueAt(row, 3).toString());
                    Object ghiChu = table.getValueAt(row, 4);
                    txtGhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
                }
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTONS
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(Color.WHITE);
        btnAdd = new JButton("Thêm mới");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(new JButton("Xuất PDF")); // Placeholder
        add(pnlButtons, BorderLayout.SOUTH);
    }
    
    public Department getDepartmentFromForm() {
        return new Department(txtMaPB.getText(), txtTenPB.getText(), txtDiaChi.getText(), txtSDT.getText(), txtGhiChu.getText());
    }
    
    public void showList(List<Department> list) {
        tableModel.setRowCount(0);
        for (Department d : list) {
            tableModel.addRow(new Object[]{d.getMaPB(), d.getTenPB(), d.getDiaChi(), d.getSdt(), d.getGhiChu()});
        }
    }
    
    public void addAddListener(ActionListener al) { btnAdd.addActionListener(al); }
    public void addEditListener(ActionListener al) { btnEdit.addActionListener(al); }
    public void addDeleteListener(ActionListener al) { btnDelete.addActionListener(al); }
}