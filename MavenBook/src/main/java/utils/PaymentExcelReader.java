package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PaymentExcelReader {
	

	    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) throws IOException {

	        List<Map<String, String>> dataList = new ArrayList<>();

	        FileInputStream fis = new FileInputStream(filePath);
	        XSSFWorkbook workbook = new XSSFWorkbook(fis);
	        XSSFSheet sheet = workbook.getSheet(sheetName);

	        int rowCount = sheet.getPhysicalNumberOfRows();
	        if (rowCount == 0) return dataList;

	        // Read header row
	        Row headerRow = sheet.getRow(0);
	        int colCount = headerRow.getPhysicalNumberOfCells();

	        List<String> headers = new ArrayList<>();
	        for (int j = 0; j < colCount; j++) {
	            headers.add(getCellValue(headerRow.getCell(j)));
	        }

	        // Read data rows
	        for (int i = 1; i < rowCount; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Map<String, String> rowData = new HashMap<>();
	            boolean isEmptyRow = true;

	            for (int j = 0; j < colCount; j++) {
	                String cellValue = getCellValue(row.getCell(j));
	                if (cellValue != null && !cellValue.trim().isEmpty()) {
	                    isEmptyRow = false;
	                }
	                rowData.put(headers.get(j), cellValue);
	            }

	            if (!isEmptyRow) {
	                dataList.add(rowData);
	            }
	        }

	        workbook.close();
	        fis.close();

	        return dataList;
	    }

	    private static String getCellValue(Cell cell) {
	        if (cell == null) return "";

	        switch (cell.getCellType()) {

	            case STRING:
	                return cell.getStringCellValue().trim();

	            case NUMERIC:
	                double num = cell.getNumericCellValue();
	                if (num == Math.floor(num)) {
	                    return String.valueOf((long) num);
	                } else {
	                    return String.valueOf(num);
	                }

	            case BOOLEAN:
	                return String.valueOf(cell.getBooleanCellValue());

	            case FORMULA:
	                return cell.getCellFormula();

	            case BLANK:
	                return "";

	            default:
	                return "";
	        }
	    }
	}
	
