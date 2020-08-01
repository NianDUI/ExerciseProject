package top.niandui.utils;

import top.niandui.common.expection.ReStateException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: TaskStateUtil.java
 * @description: 任务状态工具
 * @time: 2020/8/1 16:16
 * @author: liyongda
 * @version: 1.0
 */
public class TaskStateUtil {
    // 任务状态映射
    public static final Map<Integer, String> TASK_STATUS_MAP = new HashMap<>(3, 1);
    // 初始化
    static {
        TASK_STATUS_MAP.put(1, "重新获取全部任务");
        TASK_STATUS_MAP.put(2, "获取后续章节任务");
        TASK_STATUS_MAP.put(3, "重新获取单章任务");
    }

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
        String status = TASK_STATUS_MAP.get(taskstatus);
        if (status != null) {
            throw new ReStateException("正在执行" + status);
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
        String status = TASK_STATUS_MAP.get(taskstatus);
        return status == null ? "无 " + taskstatus + " 任务状态" :status;
    }
}
