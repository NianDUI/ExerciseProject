package top.niandui.common.uitls.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import org.springframework.web.multipart.MultipartFile;
import top.niandui.common.base.IBaseExcel;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @Title: EsayExcelReadUtil.java
 * @description: EsayExcelReadUtil
 * @time: 2020/1/15 15:50
 * @author: liyongda
 * @version: 1.0
 */
public class EasyExcelReadUtil {

    /**
     * 读取Excel
     *
     * @param file      上传的MultipartFile对象
     * @param dataClass 读取文件内容目标数据类的class对象
     * @param callback  回调方法，用于读取数据的保存
     * @param <T>       泛型
     */
    public static <T extends IBaseExcel<T>> void read(MultipartFile file, Class<T> dataClass, Consumer<List<T>> callback) {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            throw new RuntimeException("请检查是否是Excel文件！");
        }
        try {
            read(file.getInputStream(), dataClass, callback);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 读取Excel
     *
     * @param is        Excel文件输入流对象
     * @param dataClass 读取文件内容目标数据类的class对象
     * @param callback  回调方法，用于读取数据的保存
     * @param <T>       泛型
     */
    public static <T extends IBaseExcel<T>> void read(InputStream is, Class<T> dataClass, Consumer<List<T>> callback) {
        try {
            EasyExcel.read(is, dataClass, new CustomizeReadListener<>(dataClass, callback))
                    // 字符串、表头等数据自动trim
                    .autoTrim(Boolean.TRUE)
                    // 忽略空白行
                    .ignoreEmptyRow(Boolean.TRUE)
                    // 读取所有工作表
                    .doReadAll();
        } catch (ExcelAnalysisException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("请检查Excel数据是否正确！", e);
        }
    }

    // 定制读取处理器
    public static class CustomizeReadListener<T extends IBaseExcel<T>> extends AnalysisEventListener<T> {
        // 每隔3000条存储数据库，然后清理list ，方便内存回收
        private static final int BATCH_COUNT = 3000;
        // 数据校验器
        private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
        // 读取类对象
        private final IBaseExcel<T> iBaseExcel;
        // 读取的数据
        private final Holder<List<T>> listHolder;
        // 回调方法
        private final Holder<Consumer<List<T>>> callbackHolder;

        public CustomizeReadListener(Class<T> dataClass, Consumer<List<T>> callback) throws Exception {
            iBaseExcel = dataClass.newInstance();
            callbackHolder = new Holder<>(callback);
            listHolder = new Holder<>(new ArrayList<>());
            iBaseExcel.initDataRead(listHolder, callbackHolder);
        }

        /**
         * 每一条数据解析完成回调
         *
         * @param data 解析完的一条数据
         * @param context 读取Excel上下文
         */
        @Override
        public void invoke(T data, AnalysisContext context) {
            if (!iBaseExcel.beforeDataValidate(data, context, listHolder, callbackHolder)) {
                return;
            }
            // 执行数据校验
            Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(data);
            if (violationSet.size() > 0) {
                // 校验错误信息
                int rowIndex = context.readRowHolder().getRowIndex() + 1;
                String sheetName = context.readSheetHolder().getSheetName();
                StringBuilder message = new StringBuilder("'" + sheetName + "'工作表-第" + rowIndex + "行：");
                for (ConstraintViolation<T> violation : violationSet) {
                    message.append(violation.getMessage()).append("、");
                }
                throw new RuntimeException(message.substring(0, message.length() - 1));
            }
            if (!iBaseExcel.afterDataValidate(data, context, listHolder)) {
                return;
            }
            List<T> list = listHolder.value;
            list.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (list.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                list.clear();
            }
        }

        /**
         * 所有数据解析完成回调
         *
         * @param context 读取Excel上下文
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
            iBaseExcel.afterDataAllRead(context, listHolder, callbackHolder);
        }

        /**
         * 保存数据回调
         */
        private void saveData() {
            List<T> list = listHolder.value;
            if (list.size() > 0 && iBaseExcel.beforeDataSave(listHolder, callbackHolder)) {
                // 回调方法
                callbackHolder.value.accept(list);
                iBaseExcel.afterDataSave(listHolder, callbackHolder);
            }
        }
    }
}
