package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ItemExcelReader {

	public static Object[][] getTestData(String filePath, String sheetName) throws IOException {
	    FileInputStream fis = new FileInputStream(filePath);
	    XSSFWorkbook workbook = new XSSFWorkbook(fis);
	    XSSFSheet sheet = workbook.getSheet(sheetName);

	    int rowCount = sheet.getPhysicalNumberOfRows();
	    int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

	    List<Object[]> dataList = new ArrayList<>();

	    for (int i = 1; i < rowCount; i++) {
	        XSSFRow row = sheet.getRow(i);
	        if (row == null) continue; // skip empty/deleted rows

	        // Check if entire row is empty
	        boolean isEmptyRow = true;
	        Object[] rowData = new Object[colCount];

	        for (int j = 0; j < colCount; j++) {
	            XSSFCell cell = row.getCell(j);
	            String cellValue = getCellValue(cell);
	            rowData[j] = cellValue;

	            if (cellValue != null && !cellValue.trim().isEmpty()) {
	                isEmptyRow = false;
	            }
	        }

	        if (!isEmptyRow) {
	            dataList.add(rowData);
	        }
	    }

	    workbook.close();
	    fis.close();

	    // Convert list to array
	    Object[][] data = new Object[dataList.size()][colCount];
	    for (int i = 0; i < dataList.size(); i++) {
	        data[i] = dataList.get(i);
	    }
//	    System.out.println("Excel Data Loaded:");
//	    for (Object[] row : dataList) {
//	        System.out.println(Arrays.toString(row));
//	    }

	    return data;
	}
	 private static String getCellValue(XSSFCell cell) {
	        if (cell == null) return "";
	        switch (cell.getCellType()) {
	            case STRING:
	                return cell.getStringCellValue().trim();
	            case NUMERIC:
	                // Remove .0 for whole numbers
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
	            default:
	                return "";
	        }
	    }

}
