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
        
        String roleName = "0".equals(role) ? "Admin" : "User";
        this.view.setRole(roleName); 
        
        loadComboBoxData();
        loadData();
        
        this.view.addAddListener(e -> addEmployee());
        this.view.addEditListener(e -> updateEmployee());
        this.view.addDeleteListener(e -> deleteEmployee());
        this.view.addClearListener(null); 
        this.view.addExportListener(e -> exportToExcel());
    }

    private void loadComboBoxData() {
        view.updateComboBoxData(depDao.getAllDepartments(), posDao.getAllPositions());
    }

    private void loadData() {
        view.showListEmployees(empDao.getAllEmployees());
    }

    private void addEmployee() {
        Employee emp = view.getEmployeeFromForm();
        
        if (emp != null) {
            if (emp.getMaCV().isEmpty() || emp.getMaPB().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn Chức vụ và Phòng ban!");
                return;
            }
            if (empDao.addEmployee(emp)) {
                boolean accCreated = accDao.createAccount(emp.getMaNV(), "123", "1", emp.getMaNV());
                
                String msg = "Thêm nhân viên thành công!";
                if(accCreated) {
                    msg += "\nĐã tạo tài khoản tự động:\nUser: " + emp.getMaNV() + "\nPass: 123";
                } else {
                    msg += "\n(Lưu ý: Không tạo được tài khoản hoặc tài khoản đã tồn tại)";
                }
                
                JOptionPane.showMessageDialog(view, msg);
                loadData(); 
            } else {
                JOptionPane.showMessageDialog(view, "Thêm thất bại! (Có thể trùng Mã NV)");
            }
        }
    }
    
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
    
    private void deleteEmployee() {
        Employee emp = view.getEmployeeFromForm();
        
        if (emp.getMaNV() == null || emp.getMaNV().isEmpty()) {
             JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên cần xóa trên bảng!");
             return;
        }
        
        if (JOptionPane.showConfirmDialog(view, 
                "CẢNH BÁO: Bạn có chắc chắn muốn xóa nhân viên " + emp.getMaNV() + "?\n" +
                "Tài khoản đăng nhập và các dữ liệu liên quan cũng sẽ bị xóa vĩnh viễn.", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            
            if (empDao.deleteEmployee(emp.getMaNV())) {
                
                accDao.deleteAccount(emp.getMaNV());
                
                JOptionPane.showMessageDialog(view, "Đã xóa nhân viên và tài khoản liên quan!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại! Vui lòng kiểm tra lại dữ liệu.");
            }
        }
    }
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