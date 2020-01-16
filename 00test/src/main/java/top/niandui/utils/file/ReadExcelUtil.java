package top.niandui.utils.file;

//import com.synqnc.exception.ReFormatException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 读取excel工具类
 * @param <Map>
 */
public class ReadExcelUtil {

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, Cell> cellMap = new HashMap<String, Cell>();
    /**
     *
     * @Title readExcelMap
     * @Description 读Excel流返回所有sheet的Map（去除空值）
     * @param file 文件流
     * @param excelTileMap excel表头
     * @param startReadLine 开始读取的行:从0开始
     * @return Map<Integer, List <Map>>
     * @author huangwx
     * @date 2017-11-23 下午3:41:51
     */
    public Map<String, List<Map>> readMultipartExcelMap(MultipartFile file,
                                                        Map<String, String> excelTileMap, int startReadLine)
            throws Exception {
        Map<String, List<Map>> ExcelMap = new HashMap<>();
        Workbook wb = null;
        File file1 = new File("d:/aaa.xlsx");
        InputStream is = new FileInputStream(file1);
        String fileName = file1.getName();
        try {
            if (fileName.endsWith(".xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = wb.getSheetAt(sheetIndex);
                List<Map> customerList = new ArrayList<Map>();
                Row row = null;
                String[] titles = excelTileMap.get(
                        wb.getSheetName(sheetIndex)).split(",");
                for (int i = startReadLine + 1; i < sheet.getLastRowNum() + 1; i++) {
                    row = sheet.getRow(i);
                    if (isRowEmpty(row)) {
                        continue;
                    }
                    Map<String, Object> fmtmp = new HashMap<String, Object>();
                    for (int j = 0; j < titles.length; j++) {
                        try {
                            Cell c = row.getCell(j);
                            String rs = getMergedRegionValue(sheet,
                                    row.getRowNum(), c.getColumnIndex(),
                                    true);
                            if (!"".equals(rs)) {
                                fmtmp.put(titles[j].trim(), rs);
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!fmtmp.isEmpty()) {
                        customerList.add(fmtmp);
                    }
                }
                ExcelMap.put(wb.getSheetName(sheetIndex), customerList);
            }
        } catch (Exception e) {
            throw new RuntimeException("请检查Excel数据是否正确");
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                throw new RuntimeException("请检查Excel格式是否正确");
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ExcelMap;
    }

    /**
     *
     * @Title readExcelMap
     * @Description 读Excel文件返回所有sheet的Map（去除空值）
     * @param path excel所在的物理路径
     * @param excelTileMap excel表头
     * @param startReadLine 开始读取的行:从0开始
     * @param delflag 是否删除
     * @return Map<String, List < T>>
     * @author huangwx
     * @date 2017-11-23 下午3:41:51
     */
    public Map<String, List<Map>> readExcelMap(String path,
                                             Map<String, String> excelTileMap, int startReadLine, boolean delflag)
            throws Exception {
        Map<String, List<Map>> ExcelMap = new HashMap<String, List<Map>>();
        Workbook wb = null;
        InputStream is = null;
        File file = new File(path);
        try {
            is = new FileInputStream(file);
            if (path.endsWith(".xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = wb.getSheetAt(sheetIndex);
                List<Map> customerList = new ArrayList<Map>();
                Row row = null;
                String[] titles = excelTileMap.get(
                        wb.getSheetName(sheetIndex)).split(",");
                for (int i = startReadLine + 1; i < sheet.getLastRowNum() + 1; i++) {
                    row = sheet.getRow(i);
                    if (isRowEmpty(row)) {
                        continue;
                    }
                    Map<String, Object> fmtmp = new HashMap<String, Object>();
                    for (int j = 0; j < titles.length; j++) {
                        try {
                            Cell c = row.getCell(j);
                            String rs = getMergedRegionValue(sheet,
                                    row.getRowNum(), c.getColumnIndex(),
                                    true);
                            if (!"".equals(rs)) {
                                fmtmp.put(titles[j].trim(), rs);
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!fmtmp.isEmpty()) {
                        customerList.add(fmtmp);
                    }
                }
                ExcelMap.put(wb.getSheetName(sheetIndex), customerList);
            }
        } catch (Exception e) {
            throw new RuntimeException("请检查Excel数据是否正确");
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                throw new RuntimeException("请检查Excel格式是否正确");
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (delflag) {
                            file.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ExcelMap;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell, Boolean strFlag) {

        if (cell == null)
            return "";
        // 是否统一返回字符类型标记,去除返回值中多余小数点
        if (strFlag) {
            if (cell.getCellTypeEnum() == CellType.STRING) {

                return cell.getStringCellValue();

            } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {

                return String.valueOf(cell.getBooleanCellValue());

            } else if (cell.getCellTypeEnum() == CellType.FORMULA) {

                return cell.getCellFormula();

            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return formater.format(date);
                } else {
                    DecimalFormat df = new DecimalFormat("#");
                    String tmp = df.format(cell.getNumericCellValue());// 数字过长，需转换格式，如手机号码
                    // String tmp = String.valueOf(cell.getNumericCellValue());
                    return getRightStr(tmp);
                }
            }

            return "";
        } else {
            if (cell.getCellTypeEnum() == CellType.STRING) {

                return cell.getStringCellValue();

            } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {

                return String.valueOf(cell.getBooleanCellValue());

            } else if (cell.getCellTypeEnum() == CellType.FORMULA) {

                return cell.getCellFormula();

            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return formater.format(date);
                } else {
                    DecimalFormat df = new DecimalFormat("#");
                    String tmp = df.format(cell.getNumericCellValue());// 数字过长，需转换格式，如手机号码
                    // String tmp = String.valueOf(cell.getNumericCellValue());
                    return tmp;
                }
            }

            return "";
        }
    }

    // 去除多余小数点
    private String getRightStr(String sNum) {

        // DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        //
        // String resultStr = decimalFormat.format(new Double(sNum));
        //
        // if (resultStr.matches("^[-+]?\\d+\\.[0]+$")){
        //
        // resultStr = resultStr.substring(0, resultStr.indexOf("."));
        //
        // }

        if (sNum.indexOf(".") > 0) {
            sNum = sNum.substring(0, sNum.indexOf("."));
        }

        return sNum;

    }

    /**
     * 获取合并单元格值(兼容合并与非合并单元格)
     *
     * @Title
     * @Description 获取合并单元格值
     * @return String
     * @author lvl
     * @date 2017-8-22 上午9:58:23
     */
    public String getMergedRegionValue(Sheet sheet, int row, int column,
                                              Boolean strFlag) {
        if (map.size() == 0) {
            int sheetMergeCount = sheet.getNumMergedRegions();
            for (int i = 0; i < sheetMergeCount; i++) {
                CellRangeAddress range = sheet.getMergedRegion(i);
                int firstColumn = range.getFirstColumn();
                int lastColumn = range.getLastColumn();
                int firstRow = range.getFirstRow();
                int lastRow = range.getLastRow();

                for (int k = firstColumn; k <= lastColumn; k++) {
                    for (int a = firstRow; a <= lastRow; a++) {
                        Row fRow = sheet.getRow(firstRow);
                        Cell fCell = fRow.getCell(firstColumn);
                        map.put("x" + k + "y" + a, "x" + k + "y" + a);
                        cellMap.put("x" + k + "y" + a, fCell);
                    }
                }
            }
        }

        if (map.containsKey("x" + column + "y" + row)) {
            Cell fCell = cellMap.get("x" + column + "y" + row);
            return getCellValue(fCell, strFlag);
        } else {
            Row fRow = sheet.getRow(row);
            Cell fCell = fRow.getCell(column);
            return getCellValue(fCell, strFlag);
        }
    }

    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
                return false;
        }
        return true;
    }
}
