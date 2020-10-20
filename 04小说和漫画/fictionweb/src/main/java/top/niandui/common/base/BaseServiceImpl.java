package top.niandui.common.base;

import org.springframework.util.StringUtils;
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
