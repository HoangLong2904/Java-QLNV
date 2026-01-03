package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.AccountDAO; // Import thêm
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
    private AccountDAO accDao; // Khai báo thêm AccountDAO

    public EmployeeController(EmployeeView view, String role) {
        this.view = view;
        this.empDao = new EmployeeDAO();
        this.depDao = new DepartmentDAO();
        this.posDao = new PositionDAO();
        this.accDao = new AccountDAO(); // Khởi tạo
        
        this.view.setRole(role);
        loadComboBoxData();
        loadData();
            
        this.view.addEditListener(e -> updateEmployee());    
        this.view.addClearListener(null);
        this.view.addExportListener(e -> exportToExcel());
    }

    private void loadComboBoxData() {
        view.updateComboBoxData(depDao.getAllDepartments(), posDao.getAllPositions());
    }

    private void loadData() {
        view.showListEmployees(empDao.getAllEmployees());
    }

    // Giữ nguyên các hàm khác (update, export...)
    private void updateEmployee() {
        Employee emp = view.getEmployeeFromForm();
        if (emp != null) {
            if (JOptionPane.showConfirmDialog(view, "Cập nhật thông tin?") == JOptionPane.YES_OPTION) {
                if (empDao.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
                }
            }
        }
    }

    private void exportToExcel() {
        // ... (Giữ nguyên code xuất Excel cũ)
         JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            try (Workbook workbook = new XSSFWorkbook()) { 
                Sheet sheet = workbook.createSheet("Danh Sách Nhân Viên");
                Row headerRow = sheet.createRow(0);
                String[] columns = {"Mã NV", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Quê Quán", "Email", "Chức Vụ", "Phòng Ban"};
                for (int i = 0; i < columns.length; i++) headerRow.createCell(i).setCellValue(columns[i]);
                List<Employee> list = empDao.getAllEmployees();
                int rowNum = 1;
                for (Employee emp : list) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(emp.getMaNV());
                    row.createCell(1).setCellValue(emp.getHoTen());
                    row.createCell(2).setCellValue(emp.getNgaySinh().toString());
                    row.createCell(3).setCellValue(emp.getGioiTinh());
                    row.createCell(4).setCellValue(emp.getSdt());
                    row.createCell(5).setCellValue(emp.getQueQuan());
                    row.createCell(6).setCellValue(emp.getEmail());
                    row.createCell(7).setCellValue(emp.getTenChucVu());
                    row.createCell(8).setCellValue(emp.getTenPhongBan());
                }
                for(int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(view, "Xuất file Excel thành công!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi khi xuất file: " + e.getMessage());
            }
        }
    }
}