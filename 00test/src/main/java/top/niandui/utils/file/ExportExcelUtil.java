package top.niandui.utils.file;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel文档工具类
 */
public class ExportExcelUtil {
    /**
     * @description 导出excel
     * @author huangwx
     * @date 2019/12/11 16:52
     * @param response  响应对象
     * @param titleList 标题栏信息
     * 示例：[(Map){
     *          "titleName":"命令", // 表格第一行标题。是必须的
     *          "colkey":"f_command", // 存放该列数据的key值。是必须的
     *          "width":"32", // 宽度，该列的宽度。是必须的
     *          "hide":"false" // 是否隐藏，true为隐藏。不是必须的
     *          },...]
     * @param dataList  List<Map<String, Object>>。存放数据[{"f_command":"值",...},...]
     * @param fileName  下载时的文件名称
     */
    public static void exportToExcel(HttpServletResponse response,
                                     List<Map<String, Object>> titleList, List<Map> dataList, String fileName) {
        BufferedOutputStream bos = null;
        Workbook wb = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb = createWorkBook(titleList, dataList);
            wb.write(os);
            // 设置response参数，可以打开下载页面
//            response.reset();
//            response.setContentType("application/vnd.ms-excel;charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//            response.setCharacterEncoding("UTF-8");
            // 对于网络使用Buffered可以提高性能
//            bos = new BufferedOutputStream(response.getOutputStream());
            bos = new BufferedOutputStream(new FileOutputStream("d:/aaa.xlsx"));
            os.writeTo(bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wb != null)
                    wb.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param titleList
     * @param dataList
     * @return Workbook
     * @Title: createWorkBook
     * @Description:超过1048575行用多个sheet导出
     * @date 2017-5-10上午9:05:20
     */
    public static Workbook createWorkBook(List<Map<String, Object>> titleList, List<Map> dataList) {
        // 创建excel工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        // 总数据数
        int dataNum = dataList.size();
        // sheet数量
        int sheetNum = 1;
        // 一个sheet限制1048576行,超过用多个sheet放数据
        if (dataNum > 1048575) {
            sheetNum = dataNum % 1048575 == 0 ? dataNum / 1048575 : dataNum / 1048575 + 1;
        }
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
//                f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // Font f3=wb.createFont();
        // f3.setFontHeightInPoints((short) 10);
        // f3.setColor(IndexedColors.RED.getIndex());

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 4.1.1
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.CENTER);

//        // 3.14
//        // 设置第一种单元格的样式（用于列名）
//        cs.setFont(f);
//        cs.setBorderLeft(CellStyle.BORDER_THIN);
//        cs.setBorderRight(CellStyle.BORDER_THIN);
//        cs.setBorderTop(CellStyle.BORDER_THIN);
//        cs.setBorderBottom(CellStyle.BORDER_THIN);
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//        // 设置第二种单元格的样式（用于值）
//        cs2.setFont(f2);
//        cs2.setBorderLeft(CellStyle.BORDER_THIN);
//        cs2.setBorderRight(CellStyle.BORDER_THIN);
//        cs2.setBorderTop(CellStyle.BORDER_THIN);
//        cs2.setBorderBottom(CellStyle.BORDER_THIN);
//        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        // 去除隐藏列
        for (int i = 0; i < titleList.size(); i++) {
            if (Boolean.parseBoolean(titleList.get(i).get("hide") + "")) {
                titleList.remove(titleList.get(i));
            }
        }
        try {
            for (int n = 0; n < sheetNum; n++) {
                // 当前sheet数据
                List<Map> list;
                if (n == sheetNum - 1) {
                    list = dataList.subList(n * 1048575, dataNum);
                } else {
                    list = dataList.subList(n * 1048575, (n + 1) * 1048575);
                }
                // 创建sheet（页），并命名
                Sheet sheet;
                // 第一页保兼容以前格式
                if (n == 0) {
                    sheet = wb.createSheet("数据");
                } else {
                    sheet = wb.createSheet("数据" + n);
                }
                // 设置每行每列的值
                for (int i = 0; i < list.size(); i++) {
                    Map map = list.get(i);
                    // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                    // 创建一行，在页sheet上
                    Row row1 = sheet.createRow(i + 1);
                    // 在row行上创建一个方格
                    for (int j = 0; j < titleList.size(); j++) {
                        Cell cell = row1.createCell(j);
                        Object colkey = titleList.get(j).get("colkey");
                        cell.setCellValue(map.get(colkey) == null ? " " : map.get(colkey).toString());
                        cell.setCellStyle(cs2);
                    }
                }
                // 创建第一行(标题行)
                Row row = sheet.createRow((short) 0);
                // 设置列名
                for (int i = 0; i < titleList.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(titleList.get(i).get("titleName") + "");
                    cell.setCellStyle(cs);
                    Object width = titleList.get(i).get("width");
                    if (width != null) {
                        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
                        sheet.setColumnWidth(i, (int) (255.86 * (Double.valueOf(width.toString()) + 1) + 184.27));
                    } else {
                        // 自适应列宽
                        sheet.autoSizeColumn(i); //（版本不能太老）
                        // sheet.autoSizeColumn(i, true); //（合并的单元格使用）
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return wb;

    }

}