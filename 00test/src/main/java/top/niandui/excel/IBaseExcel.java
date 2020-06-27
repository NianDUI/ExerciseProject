package top.niandui.excel;

import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * @Title: IBaseExcel.java
 * @description: Excel读写实体类基本接口
 * @time: 2020/1/21 16:20
 * @author: liyongda
 * @version: 1.0
 */
public interface IBaseExcel extends SheetWriteHandler, RowWriteHandler, CellWriteHandler, NotRepeatExecutor {

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
    @Override
    default void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    default void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    // Row 回调方法
    @Override
    default void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    default void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    default void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
    }

    // Cell 回调方法
    @Override
    default void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    default void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    default void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    default void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {
    }
}
