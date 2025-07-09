import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PayReport {
	private static final String REPORT_TITLE = "Separations From Last Pay Period";
	/*
	 * private static final String REPORT_DESCRIPTION =
	 * "This report provides a list of separations from the last pay period.";
	 */ 
	private static final String REPORT_SQL = "SELECT * FROM BOX";

	public static FileReader exportToExcel() throws IOException {
		ByteArrayOutputStream bos = generateReport();
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String fileName = String.format("%s_%s.xls", REPORT_TITLE, todayDate);
		return createFileResponse(fileName, new ByteArrayInputStream(bos.toByteArray()));
	}

	private static FileReader createFileResponse(String fileName, ByteArrayInputStream byteArrayInputStream) {
		// TODO Auto-generated method stub
		return null;
	}
 
	public static ByteArrayOutputStream generateReport() throws IOException {
		/* Create an Excel workbook */ 
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			/* Create a sheet in the workbook */ 
			HSSFSheet reportSheet = workbook.createSheet("Pay Report");
			List<Map<String, Object>> reportData = null;
			//= createSQL(REPORT_SQL).fetchList();  

			/* Write the data to the sheet */ 
			writeData(reportSheet, reportData, 0); 
			if (reportSheet.getLastRowNum() > 1 && reportSheet.getRow(reportSheet.getFirstRowNum()) != null) {
				CellRangeAddress reportRegion = new CellRangeAddress(
					1, 
				reportSheet.getLastRowNum(), 0,
				reportSheet.getRow(reportSheet.getFirstRowNum()).getLastCellNum() - 1
				);
				RegionUtil.setBorderTop(BorderStyle.MEDIUM, reportRegion, reportSheet);
				RegionUtil.setBorderBottom(BorderStyle.MEDIUM, reportRegion, reportSheet);
				RegionUtil.setBorderLeft(BorderStyle.MEDIUM, reportRegion, reportSheet);
				RegionUtil.setBorderRight(BorderStyle.MEDIUM, reportRegion, reportSheet);
			}
			/* Style the header row */ 
			styleHeaderRow(reportSheet);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			return bos;
		}
	}

	private static String createSQL(String reportSql) {
		// TODO Auto-generated method stub
		return null;
	}

	/** * Apply styling to the first row (header). */
	private static void styleHeaderRow(HSSFSheet sheet) {
		HSSFPalette palette = sheet.getWorkbook().getCustomPalette();
		palette.setColorAtIndex(IndexedColors.LIGHT_BLUE.getIndex(), (byte) 47, 
		/* red */ 
		(byte) 100,
				/* green */ 
				(byte) 178 
				/* blue */ 
				);
		HSSFFont headerFont = sheet.getWorkbook().createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		HSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		Row headerRow = sheet.getRow(sheet.getFirstRowNum());
		if (headerRow != null) {
			for (int k = 0; k < headerRow.getLastCellNum(); k++) {
				if (headerRow.getCell(k) != null) {
					headerRow.getCell(k).setCellStyle(headerStyle);
				}
			}
		}
	}

	/**
	 * * Writes the data (column headers and rows) into the provided HSSFSheet. *
	 * * @param sheet The sheet where data will be written 
	 * * @param data The list of
	 * rows (maps) from the database query 
	 * * @param offset The row offset at which
	 * to start writing data
	 */
	private static void writeData(HSSFSheet sheet, List<Map<String, Object>> data, int offset) {
		AtomicInteger rowNum = new AtomicInteger(offset);
		AtomicInteger numCols = new AtomicInteger(0);
		if (!data.isEmpty()) {
			/* Collect all columns from the first row */ 
			List<String> allKeys = data.get(0).keySet().stream().collect(Collectors.toList());
			/* Create header row */ 
			Row headerRow = sheet.createRow(rowNum.getAndIncrement());
			allKeys.forEach(key -> {
				Cell cell = headerRow.createCell(numCols.getAndIncrement());
				cell.setCellValue(key);
			});
			/* Create data rows */ 
			data.forEach(rowMap -> {
				Row dataRow = sheet.createRow(rowNum.getAndIncrement());
				AtomicInteger colNumForData = new AtomicInteger(0);
				allKeys.forEach(key -> {
					Cell dataCell = dataRow.createCell(colNumForData.getAndIncrement());
					Object val = rowMap.get(key);
					if (!Objects.isNull(val) && NumberUtils.isCreatable(String.valueOf(val))) {
						dataCell.setCellValue(NumberUtils.toDouble(String.valueOf(val)));
					} else if (!Objects.isNull(val)) {
						dataCell.setCellValue(String.valueOf(val));
					} else {
						dataCell.setCellValue("");
					}
				});
			});
		}
		/* If columns exist, set an auto-filter and freeze the header row */ 
		if (numCols.get() > 0) {
			sheet.setAutoFilter(new CellRangeAddress(offset, offset, 0, numCols.get() - 1));
			sheet.createFreezePane(0, offset + 1);
			/* Auto-size columns (and widen by 35% for readability) */ 
			for (int x = 0; x < numCols.get(); x++) {
				sheet.autoSizeColumn(x);
				int currentWidth = sheet.getColumnWidth(x);
				sheet.setColumnWidth(x, currentWidth * 135 / 100);
			}
		}
	}
}