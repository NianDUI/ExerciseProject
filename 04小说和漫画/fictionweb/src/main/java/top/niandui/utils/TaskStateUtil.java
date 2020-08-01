package top.niandui.utils;

import top.niandui.common.expection.ReStateException;

/**
 * @Title: TaskStateUtil.java
 * @description: 任务状态工具
 * @time: 2020/8/1 16:16
 * @author: liyongda
 * @version: 1.0
 */
public class TaskStateUtil {

    /**
     * 检查书籍任务状态
     *
     * @param taskstatus 任务状态
     * @throws Exception
     */
    public static void checkTaskStatus(Integer taskstatus) throws ReStateException {
        if (taskstatus == null) {
            return;
        }
        switch (taskstatus) {
            case 1:
                throw new ReStateException("正在执行重新获取全部任务");
            case 2:
                throw new ReStateException("正在执行获取后续章节任务");
            case 3:
                throw new ReStateException("正在执行重新获取单章任务");
        }
    }

    /**
     * 获取任务状态说明
     *
     * @param taskstatus 任务状态
     * @return 任务状态说明
     */
    public static String getTaskStatus(Integer taskstatus) {
        if (taskstatus == null) {
            return null;
        }
        switch (taskstatus) {
            case 1:
                return "重新获取全部任务";
            case 2:
                return "获取后续章节任务";
            case 3:
                return "重新获取单章任务";
            default:
                return "无该" + taskstatus + "任务状态";
        }
    }
}
