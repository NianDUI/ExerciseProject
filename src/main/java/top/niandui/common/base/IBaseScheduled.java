package top.niandui.common.base;

import org.springframework.scheduling.Trigger;

/**
 * 基本任务接口
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/30 14:33
 */
public interface IBaseScheduled extends Trigger, Runnable {

    /**
     * 该任务是否启用
     *
     * @return t 启用、f 禁用
     */
    default boolean isEnable() {
        return true;
    }
}
