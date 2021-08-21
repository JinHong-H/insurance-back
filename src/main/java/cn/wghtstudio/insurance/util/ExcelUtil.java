package cn.wghtstudio.insurance.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExcelUtil {
    private static <T> List<Field> getClassField(Class<T> cls) {
        Field[] field = cls.getDeclaredFields();

        return Arrays.stream(field).
                filter((item) -> {
                    ExcelColumn excelColumn = item.getAnnotation(ExcelColumn.class);
                    if (excelColumn != null && excelColumn.column() != 0) {
                        item.setAccessible(true);
                        return true;
                    }
                    return false;
                }).
                sorted(Comparator.comparing((item) -> {
                    ExcelColumn excelColumn = item.getAnnotation(ExcelColumn.class);
                    if (excelColumn == null) {
                        return 0;
                    }
                    return excelColumn.column();
                })).
                collect(Collectors.toList());
    }

    private static void writeHeader(List<Field> fields, Sheet sheet, Workbook wb) {
        Row row = sheet.createRow(0);
        AtomicInteger colCount = new AtomicInteger();
        fields.forEach((item) -> {
            String columnName = "";
            ExcelColumn excelColumn = item.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
                columnName = excelColumn.value();
            }

            Cell cell = row.createCell(colCount.getAndIncrement());
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            Font font = wb.createFont();
            font.setBold(true);
            cellStyle.setFont(font);

            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnName);
        });
    }

    private static <T> void writeBody(List<T> data, List<Field> fields, Sheet sheet) {
        AtomicInteger rowCount = new AtomicInteger(1);
        data.forEach(item -> {
            AtomicInteger colCount = new AtomicInteger();
            Row row = sheet.createRow(rowCount.getAndIncrement());
            fields.forEach(field -> {
                Class<?> type = field.getType();
                Object val = "";
                try {
                    val = field.get(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                Cell cell = row.createCell(colCount.getAndIncrement());
                if (val != null) {
                    cell.setCellValue(val.toString());
                }
            });
        });
    }

    public static <T> Workbook export(List<T> data, Class<T> cls) {
        // 得到列名等信息
        List<Field> fields = ExcelUtil.getClassField(cls);

        // 创建工作表
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");

        // 写入表头
        ExcelUtil.writeHeader(fields, sheet, wb);

        // 写入数据
        ExcelUtil.writeBody(data, fields, sheet);

        return wb;
    }
}
