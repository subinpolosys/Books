package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader {

    public static List<Map<String, Object>> getMasterDetailData(String filePath, String headerSheet, String itemSheet)
            throws IOException {

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet header = workbook.getSheet(headerSheet);
        Map<String, Map<String, Object>> headerMap = new LinkedHashMap<>();

        for (int i = 1; i <= header.getLastRowNum(); i++) {
            Row row = header.getRow(i);
            if (row == null) continue;

            String referenceNo = row.getCell(0).toString();
            Map<String, Object> estimate = new HashMap<>();
            estimate.put("referenceNo", referenceNo);
            estimate.put("customerName", row.getCell(1).toString());
            estimate.put("subject", row.getCell(2).toString());
            estimate.put("items", new ArrayList<Map<String, String>>());

            headerMap.put(referenceNo, estimate);
        }

        Sheet items = workbook.getSheet(itemSheet);

        for (int i = 1; i <= items.getLastRowNum(); i++) {
            Row row = items.getRow(i);
            if (row == null) continue;

            String referenceNo = row.getCell(0).toString();
            Map<String, String> item = new HashMap<>();
            item.put("itemName", row.getCell(1).toString());
            item.put("itemQty", row.getCell(2).toString());

            if (headerMap.containsKey(referenceNo)) {
                List<Map<String, String>> itemList =
                        (List<Map<String, String>>) headerMap.get(referenceNo).get("items");
                itemList.add(item);
            }
        }

        workbook.close();
        return new ArrayList<>(headerMap.values());
    }
}
