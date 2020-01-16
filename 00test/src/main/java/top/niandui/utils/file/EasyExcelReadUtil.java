package top.niandui.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: EsayExcelReadUtil.java
 * @description: EsayExcelReadUtil
 * @time: 2020/1/15 15:50
 * @author: liyongda
 * @version: 1.0
 */
public class EasyExcelReadUtil {

    public static <T> void read(String file, Class<T> dataClass) {
        EasyExcel.read(file, dataClass, new CustomizeReadListener<T>())
                // 字符串、表头等数据自动trim
                .autoTrim(Boolean.TRUE)
                // 忽略空白行
                .ignoreEmptyRow(Boolean.TRUE)
                // 读取所有工作表
                .doReadAll();
    }

    private static class CustomizeReadListener<T> extends AnalysisEventListener<T> {
        /**
         * 每隔3000条存储数据库，然后清理list ，方便内存回收
         */
        private static final int BATCH_COUNT = 3000;
        List<T> list = new ArrayList<>();

        public CustomizeReadListener() {

        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            // 每一条数据解析都会来调用
            list.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (list.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                list.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
        }

        private void saveData() {
            if (list.size() > 0) {
//                System.out.println(list);
            }
        }
    }
}
