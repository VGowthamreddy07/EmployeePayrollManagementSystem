
import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelEmployeeRepository {
    private final File file;

    public ExcelEmployeeRepository(String filename) {
        file = new File(filename);
    }

    public List<Employee> loadAll() throws IOException {
        List<Employee> list = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            return list;
        }

        try (FileInputStream in = new FileInputStream(file);
             Workbook wb = WorkbookFactory.create(in)) {

            Sheet sheet = wb.getSheet("Employees");
            if (sheet == null) return list;

            DataFormatter fmt = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                if (r == null || text(r.getCell(0), fmt).isEmpty()) {
                    continue;
                }

                int id = (int) number(r.getCell(0), fmt, i);

                list.add(new Employee(
                    id,
                    text(r.getCell(1), fmt),
                    text(r.getCell(2), fmt),
                    text(r.getCell(3), fmt),
                    number(r.getCell(4), fmt, i),
                    number(r.getCell(5), fmt, i),
                    number(r.getCell(6), fmt, i)
                ));
            }
        } catch (RuntimeException e) {
            throw new IOException(
                "Invalid employee data in Excel: " + e.getMessage(), e
            );
        }

        return list;
    }

    public void saveAll(List<Employee> employees) throws IOException {
        Workbook workbook;

        if (file.exists() && file.length() > 0) {
            try (FileInputStream in = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(in);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        try (Workbook wb = workbook) {
            Sheet sheet = wb.getSheet("Employees");

            if (sheet == null) {
                sheet = wb.createSheet("Employees");
            }

            String[] headers = {
                "Employee ID", "Name", "Department", "Designation",
                "Basic Salary", "Allowances", "Deductions",
                "Gross Salary", "Net Salary"
            };

            Row header = sheet.getRow(0);
            if (header == null) {
                header = sheet.createRow(0);
            }

            CellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            style.setFont(font);

            for (int c = 0; c < headers.length; c++) {
                Cell cell = header.getCell(c);
                if (cell == null) cell = header.createCell(c);
                cell.setCellValue(headers[c]);
                cell.setCellStyle(style);
            }

            // Remove old data rows only after the complete
            // employee list has been loaded from Excel.
            for (int rowNum = sheet.getLastRowNum(); rowNum >= 1; rowNum--) {
                Row row = sheet.getRow(rowNum);
                if (row != null) sheet.removeRow(row);
            }

            int rowNum = 1;

            for (Employee e : employees) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getName());
                row.createCell(2).setCellValue(e.getDepartment());
                row.createCell(3).setCellValue(e.getDesignation());
                row.createCell(4).setCellValue(e.getBasicSalary());
                row.createCell(5).setCellValue(e.getAllowances());
                row.createCell(6).setCellValue(e.getDeductions());
                row.createCell(7).setCellValue(e.getGrossSalary());
                row.createCell(8).setCellValue(e.getNetSalary());
            }

            for (int c = 0; c < headers.length; c++) {
                sheet.autoSizeColumn(c);
            }

            File tempFile = new File(file.getAbsolutePath() + ".tmp");

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                wb.write(out);
            }

            // Replace the original workbook after writing succeeds.
            try {
                java.nio.file.Files.move(
                    tempFile.toPath(),
                    file.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE
                );
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                java.nio.file.Files.move(
                    tempFile.toPath(),
                    file.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                tempFile.delete();
                throw new IOException(
                    "Could not update employees.xlsx. Close Excel and retry.", e
                );
            }
        }
    }

    private static String text(Cell cell, DataFormatter formatter) {
        return cell == null
            ? ""
            : formatter.formatCellValue(cell).trim();
    }

    private static double number(
            Cell cell, DataFormatter formatter, int row) throws IOException {

        if (cell == null) {
            throw new IOException(
                "Missing numeric cell in Excel row " + (row + 1)
            );
        }

        try {
            return Double.parseDouble(
                formatter.formatCellValue(cell).trim()
            );
        } catch (NumberFormatException e) {
            throw new IOException(
                "Invalid number in Excel row " + (row + 1), e
            );
        }
    }
}
