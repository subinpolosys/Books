package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {

    /**
     * Thread-safe reader
     * Rule:
     * - Items are grouped ONLY by Vendor Name
     * - Every Master row for that Vendor gets ALL its items
     */
    public static List<Map<String, Object>> getMasterDetailData(
            String filePath,
            String masterSheetName,
            String itemSheetName) throws IOException {

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet masterSheet = workbook.getSheet(masterSheetName);
            Sheet itemSheet   = workbook.getSheet(itemSheetName);

            if (masterSheet == null || itemSheet == null) {
                throw new IllegalArgumentException("Sheet not found");
            }

            Map<Integer, String> masterHeaders = getHeaderMap(masterSheet);
            Map<Integer, String> itemHeaders   = getHeaderMap(itemSheet);

            int masterVendorCol = findColumn(
                    masterHeaders,
                    "Vendor Name",
                    "Customer Name"
            );

            int itemVendorCol = findColumn(
                    itemHeaders,
                    "Vendor Name",
                    "Customer Name"
            );

            // ================================
            // 1️⃣ Read ALL items grouped by vendor
            // ================================
            Map<String, List<Map<String, String>>> itemsByVendor = new HashMap<>();

            for (int i = 1; i <= itemSheet.getLastRowNum(); i++) {
                Row row = itemSheet.getRow(i);
                if (row == null) continue;

                String vendor = getCellValue(row, itemVendorCol);
                if (vendor.isEmpty()) continue;

                Map<String, String> itemData = new LinkedHashMap<>();
                for (Map.Entry<Integer, String> e : itemHeaders.entrySet()) {
                    itemData.put(e.getValue(), getCellValue(row, e.getKey()));
                }

                itemsByVendor
                        .computeIfAbsent(vendor, k -> new ArrayList<>())
                        .add(itemData);
            }

            // ================================
            // 2️⃣ Read Master & attach ALL vendor items
            // ================================
            List<Map<String, Object>> result = new ArrayList<>();

            for (int i = 1; i <= masterSheet.getLastRowNum(); i++) {
                Row row = masterSheet.getRow(i);
                if (row == null) continue;

                String vendor = getCellValue(row, masterVendorCol);
                if (vendor.isEmpty()) continue;

                Map<String, Object> masterData = new LinkedHashMap<>();
                for (Map.Entry<Integer, String> e : masterHeaders.entrySet()) {
                    masterData.put(e.getValue(), getCellValue(row, e.getKey()));
                }

                // IMPORTANT: copy items so threads don't share lists
                List<Map<String, String>> vendorItems =
                        itemsByVendor.getOrDefault(vendor, Collections.emptyList());

                List<Map<String, String>> itemCopy = new ArrayList<>();
                for (Map<String, String> item : vendorItems) {
                    itemCopy.add(new LinkedHashMap<>(item));
                }

                masterData.put("items", itemCopy);
                result.add(masterData);
            }

            return result;
        }
    }

    // ===================== HELPERS =====================

    private static int findColumn(Map<Integer, String> headers, String... names) {
        return headers.entrySet().stream()
                .filter(e -> {
                    String header = e.getValue().trim().replaceAll("\\s+", " ");
                    return Arrays.stream(names)
                            .anyMatch(n ->
                                    header.equalsIgnoreCase(
                                            n.trim().replaceAll("\\s+", " ")
                                    )
                            );
                })
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Column not found. Tried: " + String.join(", ", names)
                        )
                );
    }

    private static Map<Integer, String> getHeaderMap(Sheet sheet) {
        Map<Integer, String> map = new LinkedHashMap<>();
        Row header = sheet.getRow(0);
        if (header != null) {
            for (Cell cell : header) {
                map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
            }
        }
        return map;
    }

    private static String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static String getValue(Map<String, Object> data, String header) {
        Object value = data.get(header);
        return value == null ? "" : value.toString().trim();
    }
}

//public class ExcelReader {
//	/**
//     * Fully dynamic master-detail reader using compositeKey = firstColumnValue + "|" + autoId
//     */
//    public static List<Map<String, Object>> getMasterDetailData(
//            String filePath,
//            String masterSheet,
//            String itemSheet) throws IOException {
//
//        FileInputStream fis = new FileInputStream(filePath);
//        Workbook workbook = new XSSFWorkbook(fis);
//
//        Sheet master = workbook.getSheet(masterSheet);
//        Sheet items  = workbook.getSheet(itemSheet);
//
//        if (master == null || items == null) {
//            workbook.close();
//            fis.close();
//            throw new IllegalArgumentException("Sheets not found");
//        }
//
//        // ====== Build header maps dynamically ======
//        Map<Integer, String> masterColIndex = getHeaderMap(master);
//        Map<Integer, String> itemColIndex   = getHeaderMap(items);
//
//        // Use first column in master sheet as the "linking" column
//        int linkColumnIndex = masterColIndex.keySet().iterator().next(); // first column index
//        String linkColumnName = masterColIndex.get(linkColumnIndex);
//
//        // ====== Read master sheet and create compositeKey ======
//        Map<String, Map<String, Object>> masterMap = new LinkedHashMap<>();
//        for (int i = 1; i <= master.getLastRowNum(); i++) {
//            Row row = master.getRow(i);
//            if (row == null) continue;
//
//            String firstColumnValue = getCellValue(row, linkColumnIndex);
//            if (firstColumnValue.isEmpty()) continue;
//
//            String autoId = "AUTO-" + i;
//            String compositeKey = firstColumnValue + "|" + autoId;
//
//            Map<String, Object> rowData = new LinkedHashMap<>();
//            for (Map.Entry<Integer, String> entry : masterColIndex.entrySet()) {
//                String value = getCellValue(row, entry.getKey());
//                rowData.put(entry.getValue(), value);
//            }
//
//            rowData.put("autoId", autoId);
//            rowData.put("items", new ArrayList<Map<String, String>>());
//
//            masterMap.put(compositeKey, rowData);
//        }
//
//        // ====== Read item sheet and attach to master using compositeKey ======
//        for (int i = 1; i <= items.getLastRowNum(); i++) {
//            Row row = items.getRow(i);
//            if (row == null) continue;
//
//            String firstColumnValue = getCellValue(row, linkColumnIndex);
//            if (firstColumnValue.isEmpty()) continue;
//
//            // Find matching autoId from master sheet
//            String autoId = findAutoIdForCustomer(master, linkColumnIndex, firstColumnValue);
//            String compositeKey = firstColumnValue + "|" + autoId;
//
//            if (!masterMap.containsKey(compositeKey)) continue;
//
//            Map<String, String> itemData = new LinkedHashMap<>();
//            for (Map.Entry<Integer, String> entry : itemColIndex.entrySet()) {
//                String value = getCellValue(row, entry.getKey());
//                itemData.put(entry.getValue(), value);
//            }
//
//            List<Map<String, String>> itemList =
//                    (List<Map<String, String>>) masterMap.get(compositeKey).get("items");
//            itemList.add(itemData);
//        }
//
//        workbook.close();
//        fis.close();
//
//        return new ArrayList<>(masterMap.values());
//    }
//
//    // ===== Helper: get header map =====
//    private static Map<Integer, String> getHeaderMap(Sheet sheet) {
//        Map<Integer, String> map = new LinkedHashMap<>();
//        Row headerRow = sheet.getRow(0);
//        if (headerRow != null) {
//            for (Cell cell : headerRow) {
//                map.put(cell.getColumnIndex(), cell.getStringCellValue().trim());
//            }
//        }
//        return map;
//    }
//
//    // ===== Helper: find autoId for first column value =====
//    private static String findAutoIdForCustomer(Sheet masterSheet, int linkColumnIndex, String firstColumnValue) {
//        for (int i = 1; i <= masterSheet.getLastRowNum(); i++) {
//            Row row = masterSheet.getRow(i);
//            if (row == null) continue;
//            String value = getCellValue(row, linkColumnIndex);
//            if (firstColumnValue.equals(value)) {
//                return "AUTO-" + i;
//            }
//        }
//        return "AUTO-1"; // fallback
//    }
//
//    // ===== Helper: read cell safely =====
//    private static String getCellValue(Row row, int col) {
//        if (col < 0) return "";
//        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (cell == null) return "";
//
//        switch (cell.getCellType()) {
//            case STRING: return cell.getStringCellValue().trim();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) return cell.getDateCellValue().toString();
//                return String.valueOf((long) cell.getNumericCellValue());
//            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA: return cell.getCellFormula();
//            default: return "";
//        }
//    }
//    public static String getValue(Map<String, Object> data, String header) {
//        Object value = data.get(header);
//        return value == null ? "" : value.toString().trim();
//    }
	
/*	
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
*/
//}