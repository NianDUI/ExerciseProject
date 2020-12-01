package top.niandui.common.base;

import org.springframework.util.StringUtils;
import top.niandui.common.model.PageOrder;

/**
 * ServiceImpl基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 18:09
 */
public abstract class BaseServiceImpl {

    /**
     * 格式化排序参数
     *
     * @param pageOrder
     * @return
     */
    public String getOrder(PageOrder pageOrder) {
        return getOrder(pageOrder.getOrderBy(), pageOrder.getDescOrAsc());
    }

    /**
     * 格式化排序参数
     *
     * @param orderBy   排序字段
     * @param descOrAsc 排序方式
     * @return
     */
    public String getOrder(String orderBy, String descOrAsc) {
        StringBuilder order = new StringBuilder();
        if (!StringUtils.isEmpty(orderBy)) {
            if (!orderBy.startsWith("\"") && !orderBy.endsWith("\"")) {
                orderBy = "\"" + orderBy + "\"";
            }
            order.append(orderBy);
            if (!StringUtils.isEmpty(descOrAsc)) {
                order.append(" ").append(descOrAsc);
            }
        }
        return order.toString();
    }
}
