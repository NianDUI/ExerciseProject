package top.niandui.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;
import org.junit.jupiter.api.Test;
import top.niandui.common.uitls.file.EasyExcelWriteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyExcel测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/1/6 14:13
 */
public class EasyExcelTest01 {

    @Test
    public void test01() throws Exception {
        EasyExcel.write("D:/ztest/zzz.xlsx")
                // 注册写处理器
                .registerWriteHandler(new EasyExcelWriteUtil.CustomizeWriteHandler())
                // 动态设置头
                .head(head())
                // sheet名称
                .sheet("数据")
                // 数据
                .doWrite(data1());
    }

    @Test
    public void test02() throws Exception {
        EasyExcel.write("D:/ztest/zzz.xlsx")
                // 注册写处理器
                .registerWriteHandler(new EasyExcelWriteUtil.CustomizeWriteHandler())
                // 动态设置头
                .head(D.class)
                // sheet名称
                .sheet("数据")
                // 数据
                .doWrite(data2());
    }

    // 实体类对象列表作为数据
    private List<D> data2() {
        List<D> list = new ArrayList<>();
        list.add(new D("北京", " 12MB", "2", "23MB", "3"));
        list.add(new D("河asdfasdfasdasdfcxvczxcv南", "14MB", "4", "25MB", "5"));
        list.add(new D("河北", "16MB", "6", "27asdfasdfascvfawradMB", "7"));
        return list;
    }

    // 列表的列表作为数据
    private List<List<String>> data1() {
        List<List<String>> list = new ArrayList<>();
        List<String> row1 = new ArrayList<>();
        row1.add("北京");
        row1.add("12MB");
        row1.add("2");
        row1.add("24MB");
        row1.add("3");
        list.add(row1);

        List<String> row2 = new ArrayList<>();
        row2.add("河南");
        row2.add("14MB");
        row2.add("4");
        row2.add("26MB");
        row2.add("5");
        list.add(row2);

        List<String> row3 = new ArrayList<>();
        row3.add("河北");
        row3.add("16MB");
        row3.add("6");
        row3.add("27MB");
        row3.add("7");
        return list;
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("省份");
        list.add(head0);

        List<String> head1 = new ArrayList<>();
        head1.add("2020/12/30");
        head1.add("文件大小");
        list.add(head1);
        List<String> head2 = new ArrayList<>();
        head2.add("2020/12/30");
        head2.add("文件数量");
        list.add(head2);

        head1 = new ArrayList<>();
        head1.add("2020/12/31");
        head1.add("文件大小");
        list.add(head1);
        head2 = new ArrayList<>();
        head2.add("2020/12/31");
        head2.add("文件数量");
        list.add(head2);
        return list;
    }


    @Data
    @ColumnWidth(30)
    @HeadRowHeight(30)
    @ContentRowHeight(30)
    @ExcelIgnoreUnannotated
    @HeadFontStyle(fontHeightInPoints = 16, color = Font.COLOR_RED)
    @ContentFontStyle(fontHeightInPoints = 16, color = Font.COLOR_RED)
    public class D {
        @ExcelProperty("省份")
        private String col0;
        @ExcelProperty({"2020/12/30", "文件大小"})
        private String col1;
        @ExcelProperty({"2020/12/30", "文件数量"})
        private String col2;
        @ExcelProperty({"2020/12/31", "文件大小"})
        private String col3;
        @ExcelProperty({"2020/12/31", "文件数量"})
        private String col4;

        public D(String col0, String col1, String col2, String col3, String col4) {
            this.col0 = col0;
            this.col1 = col1;
            this.col2 = col2;
            this.col3 = col3;
            this.col4 = col4;
        }
    }

}
