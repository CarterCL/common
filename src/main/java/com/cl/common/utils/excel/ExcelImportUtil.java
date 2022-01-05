package com.cl.common.utils.excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel导入工具类
 *
 * @author zhangjj
 * 2022/1/5
 */
public class ExcelImportUtil {

    /**
     * 解析excel
     *
     * @param file 文件
     * @return 数据列表
     */
    public static List<List<Object>> analyzeExcelForList(File file) {
        try {
            InputStream in = new FileInputStream(file);
            return ExcelImportUtil.analyzeExcelForList(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 解析excel
     *
     * @param in 文件二进制输入流
     * @return 数据列表
     */
    public static List<List<Object>> analyzeExcelForList(InputStream in) {
        try {
            List<List<Object>> dataList = new ArrayList<>();

            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(in);

            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }

                // 循环行Row
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        break;
                    }
                    // 循环列Cell
                    List<Object> dList = new ArrayList<>();
                    for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell hssfCell = hssfRow.getCell((short) cellNum);
                        dList.add(getValue(hssfCell));
                    }
                    dataList.add(dList);
                }
            }
            // 如果is不为空，则关闭InputSteam文件输入流
            if (in != null) {
                in.close();
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /*    public static List<Map<String, String>> analyzeExcelForlistMap(MultipartFile file) {
        try {
            List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
            List<String> titleList = new ArrayList<String>();
            // 读取excel工作簿
            XSSFWorkbook hssfWorkbook = new XSSFWorkbook(file.getInputStream());

            // 循环工作表Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }

                // 循环行Row
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        break;
                    }
                    // 循环列Cell
                    Map<String, String> dataMap = new HashMap<String, String>();
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell hssfCell = hssfRow.getCell((short) cellNum);
                        if (rowNum == 0) {
                            titleList.add(getValue(hssfCell));
                        } else {
                            dataMap.put(titleList.get(cellNum), getValue(hssfCell));
                        }
                    }
                    dataList.add(dataMap);
                }
            }

            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    private static String getValue(XSSFCell hssfCell) {
        String cellValue = null;
        if (hssfCell == null) {
            return null;
        }
        if (hssfCell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(hssfCell)) {
                Date date = hssfCell.getDateCellValue();
                cellValue = parseDateToString(date, "yyyy-MM-dd");
            } else {
                BigDecimal bd = new BigDecimal(hssfCell.getNumericCellValue());
                cellValue = bd.toPlainString();
            }
        } else if (hssfCell.getCellType() == CellType.STRING) {
            cellValue = hssfCell.getStringCellValue();
        }
        return cellValue;
    }

    private static String parseDateToString(Date dateValue, String strFormat) {
        if (dateValue == null || strFormat == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(strFormat);

        return dateFormat.format(dateValue);
    }

}
