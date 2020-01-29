package top.niandui.model;

import top.niandui.utils.WebClientUtil;

/**
 * @Title: IBaseComponent.java
 * @description: IBaseComponent
 * @time: 2020/1/29 12:47
 * @author: liyongda
 * @version: 1.0
 */
public interface IBaseComponent {

    /**
     * 开始前打印
     */
    default void startBeforePrint() {
    }

    /**
     * 开始方法
     */
    default void start() {
        startBeforePrint();
        WebClientUtil.importInfo(getInfo());
    }

    /**
     * 组装Info对象
     * @return
     */
    Info getInfo();
}
