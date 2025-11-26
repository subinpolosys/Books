package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class CustomerExcelReader {
	
	   public static List<Map<String, Object>> getTestData(String filePath, String sheet1, String sheet2, String sheet3) throws IOException {
	        List<Map<String, Object>> combinedData = new ArrayList<>();

	        try (FileInputStream fis = new FileInputStream(filePath);
	             Workbook workbook = new XSSFWorkbook(fis)) {

	            Sheet s1 = workbook.getSheet(sheet1);
	            Sheet s2 = workbook.getSheet(sheet2);
	            Sheet s3 = workbook.getSheet(sheet3);

	            if (s1 == null || s2 == null || s3 == null) {
	                throw new IllegalArgumentException("One or more sheets not found in Excel file.");
	            }
	            // read headers dynamically
	            List<String> headers1 = getHeaders(s1);
	            List<String> headers2 = getHeaders(s2);
	            List<String> headers3 = getHeaders(s3);

	            int totalRows = Math.max(Math.max(s1.getLastRowNum(), s2.getLastRowNum()), s3.getLastRowNum());

	            // combine row-wise data from all three sheets
	            for (int i = 1; i <= totalRows; i++) {
	                Map<String, Object> record = new LinkedHashMap<>();

	                mergeRowData(s1, i, headers1, record);
	                mergeRowData(s2, i, headers2, record);
	                mergeRowData(s3, i, headers3, record);

	                // skip empty rows (if no data from all three)
	                if (!record.isEmpty())
	                    combinedData.add(record);
	            }
	        }
	        return combinedData;
	    }

	    // helper to read headers from sheet
	    private static List<String> getHeaders(Sheet sheet) {
	        List<String> headers = new ArrayList<>();
	        Row headerRow = sheet.getRow(0);
	        if (headerRow != null) {
	            for (Cell cell : headerRow) {
	                headers.add(getCellValue(cell));
	            }
	        }
	        return headers;
	    }
	    // helper to merge data from a single row into combined map
	    private static void mergeRowData(Sheet sheet, int rowIndex, List<String> headers, Map<String, Object> record) {
	        Row row = sheet.getRow(rowIndex);
	        if (row == null) return;

	        for (int i = 0; i < headers.size(); i++) {
	            String header = headers.get(i);
	            String value = getCellValue(row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
	            if (header != null && !header.trim().isEmpty() && !value.isEmpty()) {
	                record.put(header, value);
	            }
	        }
	    }

	    // helper to safely read a cell
	    private static String getCellValue(Cell cell) {
	        if (cell == null) return "";
	        switch (cell.getCellType()) {
	            case STRING:
	                return cell.getStringCellValue().trim();
	            case NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell))
	                    return cell.getDateCellValue().toString();
	                else
	                    return String.valueOf(cell.getNumericCellValue());
	            case BOOLEAN:
	                return String.valueOf(cell.getBooleanCellValue());
	            case FORMULA:
	                try {
	                    return cell.getStringCellValue();
	                } catch (Exception e) {
	                    return String.valueOf(cell.getNumericCellValue());
	                }
	            default:
	                return "";
	        }
	    }
	
	
}
