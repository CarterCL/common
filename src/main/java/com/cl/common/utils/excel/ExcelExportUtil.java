package com.cl.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * excel导出工具类-带样式
 *
 * @author zhangjj
 * 2022/1/5
 */
public class ExcelExportUtil {

    /**
     * excel导出方法
     *
     * @param sheetName  sheet名称
     * @param dataList   数据列表-实体类list
     * @param headers    标题数据
     * @param fieldNames 实体类对应的字段名列表
     * @return XSSFWorkbook
     */
    public static XSSFWorkbook exportExcel(String sheetName, List<Map<String, String>> dataList, String[] headers, String[] fieldNames) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        workbook.setSheetName(0, sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 25);

        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        initCellStyle(style);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        initCellStyle(style2);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBold(false);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell;
        for (short i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.getString());
        }

        Object cellVal;
        if (null != dataList) {
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, String> t = dataList.get(i);

                row = sheet.createRow(i + 1);

                for (short j = 0; j < fieldNames.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(style2);
                    cellVal = t.get(fieldNames[j]);
                    cell.setCellValue(cellVal == null ? "" : cellVal.toString());
                }
            }
        }
        return workbook;
    }

    private static void initCellStyle(XSSFCellStyle style) {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
    }

}
