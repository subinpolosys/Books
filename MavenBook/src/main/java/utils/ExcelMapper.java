package utils;

import java.util.Map;

public class ExcelMapper {

    public static String get(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value == null ? null : value.toString().trim();
    }
}
