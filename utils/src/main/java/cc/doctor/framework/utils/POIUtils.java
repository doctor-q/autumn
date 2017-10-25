package cc.doctor.framework.utils;

import cc.doctor.framework.entity.Function;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by doctor on 2017/7/30.
 */
public class POIUtils {
    public static final Logger log = LoggerFactory.getLogger(POIUtils.class);
    private static Map<String, Function> colFunctions = new HashMap<>();

    public static void addFunction(String field, Function function) {
        colFunctions.put(field, function);
    }

    public static <T> void exportExcel(String title, String[] headers, Iterable<T> dataSet, OutputStream out) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title);
        int index = 0;
        if (headers != null) {
            HSSFRow row = sheet.createRow(index++);
            for (short i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            HSSFRow row = sheet.createRow(index ++);
            T t = it.next();
            if (t instanceof Map) {
                Map map = (Map) t;
                int i = 0;
                for (Object k : map.keySet()) {
                    HSSFCell cell = row.createCell(i ++);
                    Object value = map.get(k);
                    Function function = colFunctions.get(k.toString());
                    if (function != null) {
                        value = function.tranform(value);
                    }
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            } else {
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    try {
                        Object value = ReflectUtils.get(fieldName, t);
                        Function function = colFunctions.get(fieldName);
                        if (function != null) {
                            value = function.tranform(value);
                        }
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                    } catch (SecurityException e) {
                        log.error("", e);
                    }
                }
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

}
