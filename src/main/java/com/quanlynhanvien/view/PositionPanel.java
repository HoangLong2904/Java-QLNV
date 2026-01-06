package com.quanlynhanvien.view;

import com.quanlynhanvien.model.Position;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat; 
import java.util.List;

public class PositionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaCV, txtTenCV, txtLuong;
    private JTextArea txtGhiChu;
    private JButton btnAdd, btnEdit, btnDelete;
    
    private DecimalFormat df = new DecimalFormat("#,###");

    public PositionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin chức vụ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; pnlTop.add(new JLabel("Mã chức vụ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtMaCV = new JTextField(15); pnlTop.add(txtMaCV, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlTop.add(new JLabel("Lương cơ bản (VNĐ):"), gbc); // Thêm đơn vị vào nhãn
        gbc.gridx = 1; gbc.gridy = 1; txtLuong = new JTextField(15); pnlTop.add(txtLuong, gbc);
        gbc.gridx = 2; gbc.gridy = 0; pnlTop.add(new JLabel("Tên chức vụ:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; txtTenCV = new JTextField(15); pnlTop.add(txtTenCV, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlTop.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; 
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlTop.add(txtGhiChu, gbc);

        add(pnlTop, BorderLayout.NORTH);

        String[] columns = {"Mã CV", "Tên Chức Vụ", "Lương Cơ Bản", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaCV.setText(table.getValueAt(row, 0).toString());
                    txtTenCV.setText(table.getValueAt(row, 1).toString());
                    txtLuong.setText(table.getValueAt(row, 2).toString());                   
                    Object ghiChu = table.getValueAt(row, 3);
                    txtGhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
                }
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(Color.WHITE);
        btnAdd = new JButton("Thêm mới");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        add(pnlButtons, BorderLayout.SOUTH);
    }
    public Position getPositionFromForm() {
        double luong = 0;
        try {
            String luongStr = txtLuong.getText().trim().replace(",", "").replace(".", "");
            if (!luongStr.isEmpty()) {
                luong = Double.parseDouble(luongStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số!");
            luong = 0; 
        }
        return new Position(txtMaCV.getText(), txtTenCV.getText(), luong, txtGhiChu.getText());
    }
    public void showList(List<Position> list) {
        tableModel.setRowCount(0);
        for (Position p : list) {
            String luongDep = df.format(p.getLuongCoBan());
            
            tableModel.addRow(new Object[]{
                p.getMaCV(), 
                p.getTenCV(), 
                luongDep, 
                p.getGhiChu()
            });
        }
    }
    
    public void addAddListener(ActionListener al) { btnAdd.addActionListener(al); }
    public void addEditListener(ActionListener al) { btnEdit.addActionListener(al); }
    public void addDeleteListener(ActionListener al) { btnDelete.addActionListener(al); }
}