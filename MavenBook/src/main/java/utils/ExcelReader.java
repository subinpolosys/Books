package utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {
    public static List<Map<String, Object>> getMasterDetailData(String filePath,
            String headerSheet,
            String itemSheet)
throws IOException {

FileInputStream fis = new FileInputStream(filePath);
Workbook workbook = new XSSFWorkbook(fis);

Sheet header = workbook.getSheet(headerSheet);
Sheet items = workbook.getSheet(itemSheet);

Map<String, Map<String, Object>> headerMap = new LinkedHashMap<>();

// ==========================
// READ HEADER SHEET
// ==========================
for (int i = 1; i <= header.getLastRowNum(); i++) {

Row row = header.getRow(i);
if (row == null) continue;

String customerName = getCellValue(row, 0);
String referenceNo  = getCellValue(row, 1);
String subject      = getCellValue(row, 2);
String salesPerson  = getCellValue(row, 3);
String invoiceDate  = getCellValue(row, 4);
String paymentTerms = getCellValue(row, 5);
String supplyDate   = getCellValue(row, 6);
String transactionType = getCellValue(row, 7);

if (customerName.isEmpty()) continue;

// ===== CREATE AUTO ID (fix for duplicate names or empty reference) =====
String autoId = "AUTO-" + i;

// Composite Key (always unique)
String compositeKey = customerName + "|" + autoId;

Map<String, Object> headerData = new LinkedHashMap<>();
headerData.put("customerName", customerName);
headerData.put("referenceNo", referenceNo);
headerData.put("subject", subject);
headerData.put("salesPerson", salesPerson);
headerData.put("invoiceDate", invoiceDate);
headerData.put("paymentTerms", paymentTerms);
headerData.put("supplyDate", supplyDate);
headerData.put("transactionType", transactionType);
headerData.put("autoId", autoId);

headerData.put("items", new ArrayList<Map<String, String>>());

headerMap.put(compositeKey, headerData);
}

// ==========================
// READ ITEM SHEET
// ==========================
for (int i = 1; i <= items.getLastRowNum(); i++) {

Row row = items.getRow(i);
if (row == null) continue;

String customerName = getCellValue(row, 0);
String itemName     = getCellValue(row, 1);
String itemQty      = getCellValue(row, 2);

if (customerName.isEmpty() || itemName.isEmpty()) continue;

// This item row also needs AUTO-ID logic
String autoId = "AUTO-" + getHeaderRowIndex(header, customerName);

String compositeKey = customerName + "|" + autoId;

if (!headerMap.containsKey(compositeKey)) continue;

Map<String, String> item = new LinkedHashMap<>();
item.put("itemName", itemName);
item.put("itemQty", itemQty);

List<Map<String, String>> itemList =
(List<Map<String, String>>) headerMap.get(compositeKey).get("items");

itemList.add(item);
}

workbook.close();
fis.close();

return new ArrayList<>(headerMap.values());
}


// helper to find row index for matching header customer
private static int getHeaderRowIndex(Sheet headerSheet, String customerName) {
for (int i = 1; i <= headerSheet.getLastRowNum(); i++) {
Row row = headerSheet.getRow(i);
if (row == null) continue;
if (customerName.equals(getCellValue(row, 0))) {
return i;
}
}
return 1; // fallback
}

// safely read cell values
private static String getCellValue(Row row, int col) {
Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
if (cell == null) return "";

switch (cell.getCellType()) {
case STRING: return cell.getStringCellValue().trim();
case NUMERIC:
if (DateUtil.isCellDateFormatted(cell))
return cell.getDateCellValue().toString();
return String.valueOf((long)cell.getNumericCellValue());
case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
case FORMULA: return cell.getCellFormula();
default: return "";
}
}
}