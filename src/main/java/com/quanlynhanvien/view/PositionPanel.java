package com.quanlynhanvien.view;

import com.quanlynhanvien.model.Position;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat; // Import thư viện định dạng số
import java.util.List;

public class PositionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtMaCV, txtTenCV, txtLuong;
    private JTextArea txtGhiChu;
    private JButton btnAdd, btnEdit, btnDelete;
    
    // Tạo định dạng số: Có dấu phẩy ngăn cách hàng nghìn, không có số thập phân vô nghĩa
    private DecimalFormat df = new DecimalFormat("#,###");

    public PositionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- FORM NHẬP LIỆU ---
        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin chức vụ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cột 1
        gbc.gridx = 0; gbc.gridy = 0; pnlTop.add(new JLabel("Mã chức vụ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtMaCV = new JTextField(15); pnlTop.add(txtMaCV, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlTop.add(new JLabel("Lương cơ bản (VNĐ):"), gbc); // Thêm đơn vị vào nhãn
        gbc.gridx = 1; gbc.gridy = 1; txtLuong = new JTextField(15); pnlTop.add(txtLuong, gbc);

        // Cột 2
        gbc.gridx = 2; gbc.gridy = 0; pnlTop.add(new JLabel("Tên chức vụ:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; txtTenCV = new JTextField(15); pnlTop.add(txtTenCV, gbc);

        // Ghi chú
        gbc.gridx = 0; gbc.gridy = 2; pnlTop.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; 
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnlTop.add(txtGhiChu, gbc);

        add(pnlTop, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"Mã CV", "Tên Chức Vụ", "Lương Cơ Bản", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        
        // Sự kiện click vào bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaCV.setText(table.getValueAt(row, 0).toString());
                    txtTenCV.setText(table.getValueAt(row, 1).toString());
                    
                    // Lấy lương đã format từ bảng, đưa lên ô nhập (giữ nguyên dấu phẩy để dễ nhìn)
                    txtLuong.setText(table.getValueAt(row, 2).toString());
                    
                    Object ghiChu = table.getValueAt(row, 3);
                    txtGhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
                }
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- NÚT BẤM ---
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
    
    // --- HÀM LẤY DỮ LIỆU TỪ FORM (Đã sửa để xử lý dấu phẩy) ---
    public Position getPositionFromForm() {
        double luong = 0;
        try {
            // Lấy text, xóa dấu phẩy hoặc chấm (nếu có) để parse thành số
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
    
    // --- HÀM HIỂN THỊ DANH SÁCH (Đã sửa để Format tiền) ---
    public void showList(List<Position> list) {
        tableModel.setRowCount(0);
        for (Position p : list) {
            // Format lương thành chuỗi có dấu phẩy (Ví dụ: 20,000,000)
            String luongDep = df.format(p.getLuongCoBan());
            
            tableModel.addRow(new Object[]{
                p.getMaCV(), 
                p.getTenCV(), 
                luongDep, // Hiển thị chuỗi đã format thay vì số gốc
                p.getGhiChu()
            });
        }
    }
    
    public void addAddListener(ActionListener al) { btnAdd.addActionListener(al); }
    public void addEditListener(ActionListener al) { btnEdit.addActionListener(al); }
    public void addDeleteListener(ActionListener al) { btnDelete.addActionListener(al); }
}