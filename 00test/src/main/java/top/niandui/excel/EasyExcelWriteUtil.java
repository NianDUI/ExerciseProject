package top.niandui.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: EasyExcelWriteUtil.java
 * @description: EasyExcelWriteUtil
 * @time: 2020/1/15 9:46
 * @author: liyongda
 * @version: 1.0
 */
public class EasyExcelWriteUtil {

    /**
     * 写出excel
     *
     * @param response  请求响应对象
     * @param dataClass 写出数据的class对象
     * @param data      写出数据的list对象
     * @param fileName  写出文件名
     * @param <T>       泛型
     */
    public static <T extends IBaseExcel> void write(HttpServletResponse response, Class<T> dataClass, List<T> data, String fileName) {
        try {
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                fileName += ".xlsx";
            }
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            // 指定写到哪，和用哪个class去写
            ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream(), dataClass);
            // 自定义的公共策略，设置自定义头部样式 和 自适应列宽
            writerBuilder.registerWriteHandler(new CustomizeWriteHandler());
            // 各自独有的策略，用于适应写各自独有的情况
            writerBuilder.registerWriteHandler(dataClass.newInstance());
            // 一个sheet的数据行数
            int rowCount = 1048575;
            if (data.size() <= rowCount) {
                // 写单个sheet
                // 设置不自动关闭流，目的是为了出错后不返回一部分信息，而是返回错误信息。
                writerBuilder.autoCloseStream(Boolean.FALSE);
                // 执行doWrite(data)后会自动关闭流
                writerBuilder.sheet("数据").doWrite(data);
            } else {
                // 写多个sheet
                // 获取ExcelWriter对象
                ExcelWriter excelWriter = writerBuilder.build();
                // 计算需要的sheet的个数
                int length = (int) Math.ceil(data.size() * 1.0 / rowCount);
                for (int i = 0; i < length; i++) {
                    // 计算部分数据索引，和设置sheet名称
                    int start, end;
                    String sheetName = "数据";
                    if (i == 0) {
                        start = 0;
                        end = rowCount;
                    } else if (i == length - 1) {
                        start = rowCount * i;
                        end = data.size();
                        sheetName += i;
                    } else {
                        start = rowCount * i;
                        end = rowCount * (i + 1);
                        sheetName += i;
                    }
                    // 写该sheet的数据，i指定sheet
                    excelWriter.write(data.subList(start, end), EasyExcel.writerSheet(i, sheetName).build());
                }
                // 关闭流
                excelWriter.finish();
            }
        } catch (Exception e) {
            // 重置response
            response.reset();
            e.printStackTrace();
            throw new RuntimeException("文件生成失败！");
        }
    }

    // 定制写处理器
    private static class CustomizeWriteHandler implements IBaseExcel {
        // 最大列宽度：单位：一个字符的1/256；最大256个字符宽度。
        private static final int MAX_COLUMN_WIDTH = 256 * 25; // 255 * 256;
        // 头部写单元格样式
        private static final WriteCellStyle HEAD_WRITE_CELL_STYLE = new WriteCellStyle();
        // 全角字符，正则匹配表达式
        private static final Pattern PATTERN = Pattern.compile("[^\\x00-\\xff]+");
        // 头部单元格样式
        private CellStyle headCellStyle;
        // sheet中的各列最大宽度
        private Map<Integer, Integer> columnMaxWidthMap = new HashMap<>();

        static {
            // 背景设置为白色
            // HEAD_WRITE_CELL_STYLE.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            // 字体设置
            WriteFont headFont = new WriteFont();
            // 设置字体大小
            headFont.setFontHeightInPoints((short) 11);
            // 是否加粗
            headFont.setBold(Boolean.FALSE);
            HEAD_WRITE_CELL_STYLE.setWriteFont(headFont);
        }

        @Override
        public String uniqueValue() {
            // 返回 CellStyleStrategy 防止它默认添加的一个 HorizontalCellStyleStrategy(extendx AbstractCellStyleStrategy) 的样式覆盖，自定义的样式
            return "CellStyleStrategy";
        }

        @Override
        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
            // 字符串、表头等数据自动trim
            writeWorkbookHolder.getWriteWorkbook().setAutoTrim(Boolean.TRUE);
            if (HEAD_WRITE_CELL_STYLE != null) {
                // 生成头部样式对象
                headCellStyle = StyleUtil.buildHeadCellStyle(writeWorkbookHolder.getWorkbook(), HEAD_WRITE_CELL_STYLE);
            }
            // 清除原有信息
            columnMaxWidthMap.clear();
        }

        @Override
        public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            // 该方法会在单元格上的所有操作完成后调用
            if (isHead != null && isHead && headCellStyle != null && head != null) {
                // 设置头部样式
                cell.setCellStyle(headCellStyle);
            }
            // 是否需要设置宽度
            boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
            if (!needSetWidth) {
                return;
            }
            // 获取单元格的数据长度
            Integer dataLength = dataLength(cellDataList, cell, isHead);
            if (dataLength < 0) {
                return;
            }
            // 计算单元格宽度
            Integer columnWidth = (int) (255.86 * (dataLength + 1) + 184.27);
            if (columnWidth > MAX_COLUMN_WIDTH) {
                // 超过最大宽度
                columnWidth = MAX_COLUMN_WIDTH;
            }
            Integer maxColumnWidth = columnMaxWidthMap.get(cell.getColumnIndex());
            // 超过现有宽度
            if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                // 更新宽度
                columnMaxWidthMap.put(cell.getColumnIndex(), columnWidth);
                // 手动设置列宽。第一个参数表示第几列，第二个参数表示列的宽度。
                writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth);
            }
        }

        @Override
        public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
            // 在该行的所有操作完成后调用。填充数据时不调用此方法。
            if (isHead != null && isHead && row != null) {
                // 将头部冻结
                writeSheetHolder.getSheet().createFreezePane(0, relativeRowIndex + 1);
            }
        }

        // 获取数据的长度
        private Integer dataLength(List<CellData> cellDataList, Cell cell, Boolean isHead) {
            if (isHead) {
                return getStringLength(cell.getStringCellValue().trim());
            }
            CellData cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            }
            switch (type) {
                case STRING:
                    return getStringLength(cellData.getStringValue().trim());
                case BOOLEAN:
                    return cellData.getBooleanValue().toString().length();
                case NUMBER:
                    String number = cellData.getNumberValue().toString();
                    // 判断是否是 3.3000000000000003 的情况
                    if (number.matches("^\\d*\\.\\d+0{7}\\d*$")) {
                        // number.indexOf('.') + 1 防止出现 3.0000000000000003 的情况
                        number = number.substring(0, number.indexOf("0000000", number.indexOf('.') + 1));
                    }
                    return number.length();
                default:
                    return -1;
            }
        }

        // 获取字符串的长度
        private Integer getStringLength(String string) {
            // 去中文后的字符串
            String str = string.replaceAll("[\\u4e00-\\u9fa5]+", "");
            // 字符串中全角字符的长度
            int emLength = 0;
            if (str.length() > 0) {
                Matcher matcher = PATTERN.matcher(str);
                while (matcher.find()) {
                    emLength += matcher.end() - matcher.start();
                }
            }
            // 原长度 + 中文长度 + 全角字符长度
            // return string.length() + (string.length() - str.length()) + emLength;
            return 2 * string.length() - str.length() + emLength;
        }
    }

}
