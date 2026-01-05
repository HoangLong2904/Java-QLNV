package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.AccountDAO;
import com.quanlynhanvien.dao.DepartmentDAO;
import com.quanlynhanvien.dao.EmployeeDAO;
import com.quanlynhanvien.dao.PositionDAO;
import com.quanlynhanvien.model.Employee;
import com.quanlynhanvien.view.EmployeeView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmployeeController {
    private EmployeeView view;
    private EmployeeDAO empDao;
    private DepartmentDAO depDao;
    private PositionDAO posDao;
    private AccountDAO accDao; 

    public EmployeeController(EmployeeView view, String role) {
        this.view = view;
        this.empDao = new EmployeeDAO();
        this.depDao = new DepartmentDAO();
        this.posDao = new PositionDAO();
        this.accDao = new AccountDAO(); 
        
        // Phân quyền giao diện: Nếu là "0" (Admin) thì cho phép sửa/xóa, "1" (User) thì chỉ xem
        // Lưu ý: Trong MainView đã truyền role vào, ta cần xử lý logic hiển thị
        String roleName = "0".equals(role) ? "Admin" : "User";
        this.view.setRole(roleName); 
        
        // 1. Tải dữ liệu ban đầu
        loadComboBoxData();
        loadData();
        
        // 2. Gán sự kiện cho các nút bấm
        this.view.addAddListener(e -> addEmployee());
        this.view.addEditListener(e -> updateEmployee());
        this.view.addDeleteListener(e -> deleteEmployee());
        
        // Nút làm mới đã được xử lý sự kiện trong View, nhưng nếu cần logic thêm có thể gán ở đây
        this.view.addClearListener(null); 
        
        this.view.addExportListener(e -> exportToExcel());
    }

    private void loadComboBoxData() {
        // Lấy danh sách Phòng ban và Chức vụ để đổ vào ComboBox trong View
        view.updateComboBoxData(depDao.getAllDepartments(), posDao.getAllPositions());
    }

    private void loadData() {
        // Lấy danh sách nhân viên từ DB và hiển thị lên Bảng
        view.showListEmployees(empDao.getAllEmployees());
    }

    // --- CHỨC NĂNG THÊM NHÂN VIÊN ---
    private void addEmployee() {
        Employee emp = view.getEmployeeFromForm();
        
        if (emp != null) {
            // Kiểm tra ràng buộc: Phải chọn Chức vụ và Phòng ban
            if (emp.getMaCV().isEmpty() || emp.getMaPB().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn Chức vụ và Phòng ban!");
                return;
            }
            
            // 1. Thêm nhân viên vào bảng NhanVien
            // Lưu ý: Nếu empDao chưa có hàm addEmployee, bạn cần cập nhật EmployeeDAO như hướng dẫn trước
            if (empDao.addEmployee(emp)) {
                
                // 2. Tự động tạo tài khoản đăng nhập
                // - Username = Mã NV
                // - Password = "123" (Mặc định)
                // - Role = "1" (User thường)
                // - MaNV = Mã NV (Để liên kết dữ liệu)
                
                // CẢNH BÁO: Nếu AccountDAO chưa cập nhật hàm createAccount nhận 4 tham số, dòng dưới sẽ báo lỗi đỏ.
                // Bạn cần sửa AccountDAO.createAccount(String user, String pass, String role, String maNV)
                boolean accCreated = accDao.createAccount(emp.getMaNV(), "123", "1", emp.getMaNV());
                
                String msg = "Thêm nhân viên thành công!";
                if(accCreated) {
                    msg += "\nĐã tạo tài khoản tự động:\nUser: " + emp.getMaNV() + "\nPass: 123";
                } else {
                    msg += "\n(Lưu ý: Không tạo được tài khoản hoặc tài khoản đã tồn tại)";
                }
                
                JOptionPane.showMessageDialog(view, msg);
                loadData(); // Tải lại bảng danh sách
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại! (Có thể trùng Mã NV)");
            }
        }
    }
    
    // --- CHỨC NĂNG CẬP NHẬT ---
    private void updateEmployee() {
        Employee emp = view.getEmployeeFromForm();
        if (emp != null) {
            if (JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn cập nhật thông tin nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (empDao.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
                }
            }
        }
    }
    
    // --- CHỨC NĂNG XÓA ---
    private void deleteEmployee() {
        // Lấy thông tin nhân viên đang được chọn (chỉ cần lấy Mã NV)
        Employee emp = view.getEmployeeFromForm();
        
        // Kiểm tra xem người dùng đã chọn nhân viên chưa (dựa vào Mã NV trên form)
        if (emp.getMaNV() == null || emp.getMaNV().isEmpty()) {
             JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên cần xóa trên bảng!");
             return;
        }
        
        if (JOptionPane.showConfirmDialog(view, 
                "CẢNH BÁO: Bạn có chắc chắn muốn xóa nhân viên " + emp.getMaNV() + "?\n" +
                "Tài khoản đăng nhập và các dữ liệu liên quan cũng sẽ bị xóa vĩnh viễn.", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            
            // 1. Xóa trong bảng NhanVien
            if (empDao.deleteEmployee(emp.getMaNV())) {
                
                // 2. Xóa tài khoản đăng nhập tương ứng trong bảng TaiKhoan
                accDao.deleteAccount(emp.getMaNV());
                
                JOptionPane.showMessageDialog(view, "Đã xóa nhân viên và tài khoản liên quan!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại! Vui lòng kiểm tra lại dữ liệu.");
            }
        }
    }

    // --- CHỨC NĂNG XUẤT EXCEL ---
    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }
            
            try (Workbook workbook = new XSSFWorkbook()) { 
                Sheet sheet = workbook.createSheet("Danh Sách Nhân Viên");
                
                // Tạo Header
                Row headerRow = sheet.createRow(0);
                String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Quê Quán", "Email", "Chức Vụ", "Phòng Ban"};
                
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Ghi dữ liệu
                List<Employee> list = empDao.getAllEmployees();
                int rowNum = 1;
                for (Employee emp : list) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(emp.getMaNV());
                    row.createCell(1).setCellValue(emp.getHoTen());
                    row.createCell(2).setCellValue(emp.getNgaySinh() != null ? emp.getNgaySinh().toString() : "");
                    row.createCell(3).setCellValue(emp.getGioiTinh());
                    row.createCell(4).setCellValue(emp.getSdt());
                    row.createCell(5).setCellValue(emp.getQueQuan());
                    row.createCell(6).setCellValue(emp.getEmail());
                    row.createCell(7).setCellValue(emp.getTenChucVu());
                    row.createCell(8).setCellValue(emp.getTenPhongBan());
                }
                
                for(int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(view, "Xuất file Excel thành công!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi xuất file: " + e.getMessage());
            }
        }
    }
}