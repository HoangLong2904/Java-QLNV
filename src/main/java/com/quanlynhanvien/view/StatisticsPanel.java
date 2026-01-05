package com.quanlynhanvien.view;

import com.quanlynhanvien.model.SalaryStatistic;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class StatisticsPanel extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> cbMonth;
    private JTextField txtYear;
    private JButton btnFilter;
    private JButton btnExportExcel;
    private JLabel lblTotalMoney; // Hiển thị tổng tiền ở chân trang

    // Định dạng tiền tệ (Ví dụ: 10,000,000)
    private DecimalFormat df = new DecimalFormat("#,###");

    public StatisticsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. THANH CÔNG CỤ (FILTER - BỘ LỌC) ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createTitledBorder("Bộ lọc thời gian"));

        pnlFilter.add(new JLabel("Tháng:"));
        cbMonth = new JComboBox<>();
        for(int i=1; i<=12; i++) cbMonth.addItem(i);
        pnlFilter.add(cbMonth);

        pnlFilter.add(new JLabel("  Năm:"));
        txtYear = new JTextField("2025", 5);
        pnlFilter.add(txtYear);

        btnFilter = new JButton("Xem Báo Cáo");
        btnFilter.setBackground(new Color(0, 102, 204)); // Xanh dương
        btnFilter.setForeground(Color.WHITE);
        pnlFilter.add(btnFilter);
        
        btnExportExcel = new JButton("Xuất Excel");
        btnExportExcel.setBackground(new Color(0, 153, 76)); // Xanh lá
        btnExportExcel.setForeground(Color.WHITE);
        pnlFilter.add(btnExportExcel);

        add(pnlFilter, BorderLayout.NORTH);

        // --- 2. BẢNG DỮ LIỆU CHI TIẾT ---
        // Các cột hiển thị trên bảng
        String[] columns = {
            "STT", "Mã nhân viên", "Họ và tên", "Phòng ban", "Chức vụ", 
            "Lương cơ bản", "Số ngày đi làm", "Phụ cấp", "Thưởng", "Tổng lương"
        };
        
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30); // Chiều cao dòng
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 240, 255));
        
        // Thêm thanh cuộn cho bảng
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 3. CHÂN TRANG (TỔNG KẾT TIỀN) ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY)); // Kẻ đường chỉ bên trên
        
        lblTotalMoney = new JLabel("Tổng lương chi trả tháng ... : 0 VNĐ");
        lblTotalMoney.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalMoney.setForeground(Color.RED);
        pnlFooter.add(lblTotalMoney);
        
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // --- HÀM HIỂN THỊ DỮ LIỆU LÊN BẢNG (Controller sẽ gọi hàm này) ---
    public void showData(List<SalaryStatistic> list) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        int stt = 1;
        double totalMonthSalary = 0;
        
        for (SalaryStatistic s : list) {
            // Logic hiển thị Phụ cấp/Thưởng:
            // Nếu Tổng lương > 0 (tức là đã được tính lương bên User) thì mới hiện Phụ cấp/Thưởng
            // Nếu chưa tính lương (Tổng lương = 0) thì Phụ cấp/Thưởng hiển thị là 0 cho đỡ rối
            double phuCap = (s.getTongLuong() > 0) ? 500000 : 0; 
            double thuong = (s.getTongLuong() > 0) ? 1000000 : 0; 
            
            tableModel.addRow(new Object[]{
                stt++,
                s.getMaNV(),
                s.getHoTen(),
                s.getPhongBan(),
                s.getChucVu(),
                df.format(s.getLuongCoBan()),
                s.getSoNgayDiLam(),     // Tính toán: 26 - số ngày nghỉ
                df.format(phuCap),      
                df.format(thuong),      
                df.format(s.getTongLuong())
            });
            
            totalMonthSalary += s.getTongLuong();
        }
        
        // Cập nhật dòng tổng cộng màu đỏ ở dưới cùng
        lblTotalMoney.setText("Tổng lương chi trả tháng " + cbMonth.getSelectedItem() + "/" + txtYear.getText() + ": " + df.format(totalMonthSalary) + " VNĐ");
    }
    
    // --- CÁC HÀM GETTER & LISTENER ---
    
    // Lấy Tháng/Năm người dùng đang chọn
    public int getSelectedMonth() { return (int) cbMonth.getSelectedItem(); }
    public int getSelectedYear() { 
        try {
            return Integer.parseInt(txtYear.getText());
        } catch (Exception e) { return 2025; } // Mặc định nếu nhập sai
    }
    
    // Lấy bảng (để Controller dùng cho chức năng Xuất Excel)
    public JTable getTable() { return table; }

    // Gắn sự kiện cho các nút bấm
    public void addFilterListener(ActionListener al) { btnFilter.addActionListener(al); }
    public void addExportListener(ActionListener al) { btnExportExcel.addActionListener(al); }
}