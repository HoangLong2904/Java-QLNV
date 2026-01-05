package com.quanlynhanvien.controller;

import com.quanlynhanvien.dao.EmployeeDAO;
import com.quanlynhanvien.model.SalaryStatistic;
import com.quanlynhanvien.view.StatisticsPanel;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StatisticsController {
    private StatisticsPanel view;
    private EmployeeDAO dao;

    public StatisticsController(StatisticsPanel view) {
        this.view = view;
        this.dao = new EmployeeDAO();

        // Mặc định load dữ liệu tháng hiện tại
        loadData();

        // Sự kiện nút
        this.view.addFilterListener(e -> loadData());
        this.view.addExportListener(e -> exportToExcel());
    }

    private void loadData() {
        int month = view.getSelectedMonth();
        int year = view.getSelectedYear();
        
        List<SalaryStatistic> list = dao.getSalaryStatistics(month, year);
        view.showData(list);
    }
    
    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Báo cáo lương");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Bảng Lương");
                JTable table = view.getTable();
                TableModel model = table.getModel();
                
                // Header
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    headerRow.createCell(i).setCellValue(model.getColumnName(i));
                }
                
                // Data
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        row.createCell(j).setCellValue(val != null ? val.toString() : "");
                    }
                }
                
                // Auto size column
                for(int i=0; i<model.getColumnCount(); i++) sheet.autoSizeColumn(i);
                
                try (FileOutputStream out = new FileOutputStream(file)) {
                    workbook.write(out);
                }
                JOptionPane.showMessageDialog(view, "Xuất file thành công!");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi xuất file: " + e.getMessage());
            }
        }
    }
}