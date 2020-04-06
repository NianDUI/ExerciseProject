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
}
