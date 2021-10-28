package top.niandui.common.base;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

import javax.xml.ws.Holder;
import java.util.List;
import java.util.function.Consumer;

/**
 * Excel读写实体类基本接口
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/1/21 16:20
 */
public interface IBaseExcel<T extends IBaseExcel<T>> extends WorkbookWriteHandler, SheetWriteHandler, RowWriteHandler, CellWriteHandler, NotRepeatExecutor {

    /**
     * 获取sheet的最后行索引
     *
     * @param writeWorkbookHolder Workbook包装对象
     * @return 最后行索引
     */
    static int getLastRowIndex(WriteWorkbookHolder writeWorkbookHolder) {
        if (writeWorkbookHolder == null) {
            return 65535;
        }
        return writeWorkbookHolder.getExcelType() == ExcelTypeEnum.XLSX ? 1048575 : 65535;
    }

    @Override
    default String uniqueValue() {
        return "IBaseExcel";
    }

    // Sheet 回调方法

    // Row 回调方法

    // Cell 回调方法

    /****************读取数据回调****************/

    // 读初始化
    default void initDataRead(Holder<List<T>> listHolder, Holder<Consumer<List<T>>> callbackHolder) {
    }

    // 读取数据校验之前
    default boolean beforeDataValidate(T data, AnalysisContext context, Holder<List<T>> listHolder, Holder<Consumer<List<T>>> callbackHolder) {
        return true;
    }

    // 读取数据校验之后
    default boolean afterDataValidate(T data, AnalysisContext context, Holder<List<T>> listHolder) {
        return true;
    }

    // 读取数据校验之前
    default boolean beforeDataSave(Holder<List<T>> listHolder, Holder<Consumer<List<T>>> callbackHolder) {
        return true;
    }

    // 读取数据校验之后
    default void afterDataSave(Holder<List<T>> listHolder, Holder<Consumer<List<T>>> callbackHolder) {
    }

    // 数据全部读取之后
    default void afterDataAllRead(AnalysisContext context, Holder<List<T>> listHolder, Holder<Consumer<List<T>>> callbackHolder) {
    }

}
