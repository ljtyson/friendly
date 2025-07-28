import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class FinancialForecastingTemplate {
    public static void main(String[] args) throws IOException {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Financial Forecast");

        // Create header row
        Row header = sheet.createRow(0);
        String[] headers = {"Month", "Revenue ($)", "Expenses ($)", "Profit ($)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            // Add styling
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Sample data rows
        String[] months = {"Jan 2023", "Feb 2023", "Mar 2023"};
        double[] revenues = {10000, 12000, 11500};
        double[] expenses = {7000, 8000, 7500};
        for (int i = 0; i < months.length; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(months[i]);
            row.createCell(1).setCellValue(revenues[i]);
            row.createCell(2).setCellValue(expenses[i]);
            row.createCell(3).setCellFormula("B" + (i + 2) + "-C" + (i + 2));
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Save the workbook
        try (FileOutputStream fileOut = new FileOutputStream("FinancialForecastTemplate.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
