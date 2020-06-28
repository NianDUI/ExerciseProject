package top.niandui.common.base;

import org.springframework.util.StringUtils;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.model.PageOrder;

/**
 * @Title: BaseServiceImpl.java
 * @description: ServiceImpl基类
 * @time: 2020/3/22 18:09
 * @author: liyongda
 * @version: 1.0
 */
public class BaseServiceImpl {

    /**
     * 格式化排序参数
     *
     * @param pageOrder
     * @return
     */
    public String getOrder(PageOrder pageOrder) {
        StringBuilder order = new StringBuilder();
        String orderBy = pageOrder.getOrderBy();
        if (!StringUtils.isEmpty(orderBy)) {
            order.append(orderBy);
            String descOrAsc = pageOrder.getDescOrAsc();
            if (!StringUtils.isEmpty(descOrAsc)) {
                order.append(" ").append(descOrAsc);
            }
        }
        return order.toString();
    }

    /**
     * 检查书籍任务状态
     *
     * @param taskstatus 任务状态
     * @throws Exception
     */
    public static void checkTaskStatus(Integer taskstatus) throws Exception {
        if (taskstatus == null) {
            return;
        }
        switch (taskstatus) {
            case 1 : throw new ReStateException("正在执行重新获取全部任务");
            case 2 : throw new ReStateException("正在执行获取后续章节任务");
            case 3 : throw new ReStateException("正在执行重新获取单章任务");
        }
    }
}
