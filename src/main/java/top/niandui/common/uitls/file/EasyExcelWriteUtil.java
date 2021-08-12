package top.niandui.common.uitls.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import top.niandui.common.base.IBaseExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.niandui.common.uitls.MethodUtil.batch;
import static top.niandui.common.uitls.file.DownloadUtil.getDownloadOS;

/**
 * 阿里EasyExcel写工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/1/15 9:46
 */
public class EasyExcelWriteUtil {
    /**
     * 写单个Sheet到Excel
     *
     * @param response 请求响应对象
     * @param fileName 写出文件名
     * @param data     写出数据的list对象
     * @param <T>      泛型
     */
    public static <T extends IBaseExcel<T>> void write(HttpServletResponse response, String fileName, List<T> data) {
        write(response, fileName, null, data);
    }

    /**
     * 写单个Sheet到Excel
     *
     * @param response  请求响应对象
     * @param fileName  写出文件名
     * @param dataClass 写出数据的class对象
     * @param data      写出数据的list对象
     * @param <T>       泛型
     */
    public static <T extends IBaseExcel<T>> void write(HttpServletResponse response, String fileName, Class<T> dataClass, List<T> data) {
        String[] sheets = {"数据"};
        write(response, fileName, sheets, new Class[]{dataClass}, Collections.singletonMap(sheets[0], data));
    }

    /**
     * 写单个Sheet到Excel
     *
     * @param os   输出流
     * @param data 写出数据的list对象
     * @param <T>  泛型
     */
    public static <T extends IBaseExcel<T>> void write(OutputStream os, List<T> data) {
        write(os, null, data);
    }

    /**
     * 写单个Sheet到Excel
     *
     * @param os        输出流
     * @param dataClass 写出数据的class对象
     * @param data      写出数据的list对象
     * @param <T>       泛型
     */
    public static <T extends IBaseExcel<T>> void write(OutputStream os, Class<T> dataClass, List<T> data) {
        String[] sheets = {"数据"};
        write(os, sheets, new Class[]{dataClass}, Collections.singletonMap(sheets[0], data));
    }

    /**
     * 写多个不同Sheet到Excel
     *
     * @param response 请求响应对象
     * @param fileName 写出文件名
     * @param sheets   写到Excel的Sheet名称数组
     * @param data     写到Excel的Sheet名称和数据列表映射Map
     * @param <T>      IBaseExcel及其子类
     */
    public static <T extends IBaseExcel<T>> void write(HttpServletResponse response, String fileName, String[] sheets, Map<String, List<T>> data) {
        write(response, fileName, sheets, null, data);
    }

    /**
     * 写多个不同Sheet到Excel
     *
     * @param response 请求响应对象
     * @param fileName 写出文件名
     * @param sheets   写到Excel的Sheet名称数组
     * @param classes  写到Excel的Sheet名称对应Class对象数组
     * @param data     写到Excel的Sheet名称和数据列表映射Map
     * @param <T>      IBaseExcel及其子类
     */
    public static <T extends IBaseExcel<T>> void write(HttpServletResponse response, String fileName, String[] sheets, Class<T>[] classes, Map<String, List<T>> data) {
        try {
            int index = fileName.lastIndexOf(".");
            fileName = (index > 0 ? fileName.substring(0, index) : fileName) + ".xlsx";
            write(getDownloadOS(response, fileName), sheets, classes, data);
        } catch (IOException e) {
            // 重置response
//            response.reset();
            throw new RuntimeException("文件生成失败！", e);
        }
    }

    /**
     * 写多个不同Sheet到Excel
     *
     * @param sheets 写到Excel的Sheet名称数组
     * @param data   写到Excel的Sheet名称和数据列表映射Map
     * @param <T>    IBaseExcel及其子类
     */
    public static <T extends IBaseExcel<T>> void write(OutputStream os, String[] sheets, Map<String, List<T>> data) {
        write(os, sheets, null, data);
    }

    /**
     * 写多个不同Sheet到Excel
     *
     * @param sheets  写到Excel的Sheet名称数组
     * @param classes 写到Excel的Sheet名称对应Class对象数组
     * @param data    写到Excel的Sheet名称和数据列表映射Map
     * @param <T>     IBaseExcel及其子类
     */
    public static <T extends IBaseExcel<T>> void write(OutputStream os, String[] sheets, Class<T>[] classes, Map<String, List<T>> data) {
        if (sheets != null && classes != null && sheets.length != classes.length) {
            throw new RuntimeException("sheets和classes长度不相等请检查");
        }
        try (OutputStream _os = os) {
            // 指定写到哪
            ExcelWriterBuilder writerBuilder = EasyExcel.write(_os);
            // 自定义的公共策略，设置自定义头部样式 和 自适应列宽
            writerBuilder.registerWriteHandler(new CustomizeWriteHandler());
            ExcelWriter excelWriter = writerBuilder.build();
            List<List<String>> emptyHead = Collections.singletonList(Collections.singletonList("暂无数据"));
            int i = 0;
            int[] num = {0};
            for (String key : sheets) {
                num[0] = 0;
                batch(data.get(key), ts -> {
                    String sheetName = num[0] > 0 ? key + num[0] : key;
                    // 写该sheet的数据
                    T t = ts.get(0);
                    WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).registerWriteHandler(t).head(t.getClass()).build();
                    excelWriter.write(ts, writeSheet);
                    num[0]++;
                    return ts.size();
                }, 1048575);
                if (num[0] == 0) {
                    WriteSheet writeSheet;
                    if (classes == null || classes[i] == null) {
                        writeSheet = EasyExcel.writerSheet(key).head(emptyHead).build();
                    } else {
                        writeSheet = EasyExcel.writerSheet(key).head(classes[i]).build();
                    }
                    excelWriter.write(Collections.emptyList(), writeSheet);
                }
                i++;
            }
            if (i == 0) {
                excelWriter.write(Collections.emptyList(), EasyExcel.writerSheet("数据").head(emptyHead).build());
            }
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // 定制写处理器, 自定义的公共策略，设置自定义头部样式 和 自适应列宽
    public static class CustomizeWriteHandler implements IBaseExcel<CustomizeWriteHandler> {
        // 最大列宽度：单位：一个字符的1/256；最大256个字符宽度。
        private static final int MAX_COLUMN_WIDTH = 256 * 25; // 255 * 256;
        // 头部写单元格样式
        private static final WriteCellStyle HEAD_WRITE_CELL_STYLE = new WriteCellStyle();
        // 全角字符，正则匹配表达式
        private static final Pattern PATTERN = Pattern.compile("[^\\x00-\\xff]+");

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

        // sheet中的各列最大宽度
        private final Map<Integer, Integer> columnMaxWidthMap = new HashMap<>();
        // 头部单元格样式
        private CellStyle headCellStyle;

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
