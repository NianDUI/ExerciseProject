package top.niandui.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.junit.Test;
import top.niandui.utils.file.EasyExcelReadUtil;
import top.niandui.utils.file.EasyExcelWriteUtil;
import top.niandui.utils.file.ExportExcelUtil;
import top.niandui.utils.file.ReadExcelUtil;

import java.util.*;

/**
 * @Title: ExportExcelTest.java
 * @description: ExportExcelTest
 * @time: 2020/1/13 17:05
 * @author: liyongda
 * @version: 1.0
 */
public class ExportExcelTest {

    public static void main(String[] args) {

        ArrayList<Map<String, Object>> tiles = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("titleName", "命令");
        map.put("colkey", "f_command");
        map.put("width", "32");
        tiles.add(map);

        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 1048577; i++) {
            Map<String, Object> mapData = new HashMap<String, Object>();
            mapData.put("f_command", i + 1 + "");
            list.add(mapData);
        }
        Long start = System.currentTimeMillis();
        ExportExcelUtil.exportToExcel(null, tiles, list, "");
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println((end - start) / 1000);
    }

    @Test
    public void poiReadExcel() throws Exception {
        Map<String, String> tileMap = new HashMap<String, String>();
        tileMap.put("数据", "f_command");
        tileMap.put("数据1", "f_command");
        // 读取excel数据
        ReadExcelUtil rm = new ReadExcelUtil();
        Long start = System.currentTimeMillis();
        Map<String, List<Map>> excelMap = rm.readMultipartExcelMap(null, tileMap, 0);
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println((end - start) / 1000);
    }


    @Test
    public void easyExcelWriteTest() {
        List<DemoData> demoData = data1();
        Long start = System.currentTimeMillis();
        try {
            EasyExcelWriteUtil.write(null, DemoData.class, demoData, "d:/bbb.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println((end - start) / 1000);
    }

    public List<DemoData> data1() {
        List<DemoData> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            DemoData demoData = new DemoData();
            demoData.string = "测试字符串" + (i + 1) + "？？:：";
            demoData.data = new Date();
            demoData.doubleData = i * 1.1;
            demoData.integer = i;
            demoData.byteData = (byte) i;
            demoData.shortData = (short) i;
            demoData.longData = (long) i;
            demoData.floatData = (float) i;
            demoData.booleanData = i % 2 == 0 ? true : false;
            data.add(demoData);
        }
        data.get(13).string = "asdfasdfasdfasdfasdfa案发后SDK炬华科技h’【【。1】2a/.111111111111111111111111111111111111111111111111111111111111111" +
                "1222222222222222222222222222222222222";
        return data;
    }

    @Data
    public static class DemoData {
//        @ColumnWidth(25)
        @ExcelProperty(value = "命令")
        private String string;
//        @ExcelIgnore
        @ExcelProperty(value = {"英文标题", "data"})
        private Date data;
//        @ExcelIgnore
        @ExcelProperty(value = {"英文标题", "doubleData"})
        private Double doubleData;
        private Integer integer;
        private Byte byteData;
        private Short shortData;
        private Long longData;
        private Float floatData;
        @ExcelProperty(value = "布尔值类型的数据")
        private Boolean booleanData;
        @ExcelIgnore
        private String ignore;
        @ExcelIgnore
        private InnerClass innerClass = new InnerClass();
    }

    @Data
    public static class InnerClass {
        private String str = "innerClass";
        private Integer i = 0;
    }

    @Test
    public void easyExcelReadTest() {
        Long start = System.currentTimeMillis();
        EasyExcelReadUtil.read("d:/bbb.xlsx", DemoData.class);
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println((end - start) / 1000);
    }

}
