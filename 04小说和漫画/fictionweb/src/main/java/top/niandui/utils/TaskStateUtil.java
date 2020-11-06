package top.niandui.utils;

import top.niandui.common.expection.ReStateException;

import java.util.HashMap;
import java.util.Map;

import static top.niandui.common.uitls.MethodUtil.convert;
import static top.niandui.config.PublicBean.iBookDao;
import static top.niandui.config.PublicBean.redisUtil;
import static top.niandui.config.PublicConstant.BOOK_TASK_STATUS;

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
     * 获取书籍任务状态
     *
     * @param bookid 书籍id
     * @return 返回书籍任务状态
     */
    public static Integer getBookTaskStatus(Long bookid) {
        Integer taskstatus = convert(redisUtil.hget(BOOK_TASK_STATUS, bookid + ""), Integer::valueOf, Integer.class);
        if (taskstatus == null) {
            taskstatus = iBookDao.queryBookTaskstatus(bookid);
            redisUtil.hset(BOOK_TASK_STATUS, bookid + "", taskstatus);
        }
        return taskstatus;
    }

    /**
     * 更新任务状态
     *
     * @param bookid     书籍id
     * @param taskstatus 要修改的任务状态
     * @return 更新行数
     */
    public static int updateBookTaskStatus(Long bookid, Integer taskstatus) {
        redisUtil.hset(BOOK_TASK_STATUS, bookid + "", taskstatus);
        return iBookDao.updateTaskstatus(bookid, taskstatus);
    }

    /**
     * 更新指定状态任务的任务状态
     *
     * @param bookid    书籍id
     * @param newStatus 要修改的任务状态
     * @param rawStatus 原任务状态
     * @return 更新行数
     */
    public static int updateTaskStatusByRawStatus(Long bookid, Integer newStatus, Integer rawStatus) {
        int num = iBookDao.updateTaskstatusByRawStatus(bookid, newStatus, rawStatus);
        if (num > 0) {
            redisUtil.hset(BOOK_TASK_STATUS, bookid + "", newStatus);
        }
        return num;
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
        return status == null ? "无 " + taskstatus + " 任务状态" : status;
    }


    /**
     * 检查书籍任务开关
     *
     * @param taskswitch 任务开关
     * @throws Exception
     */
    public static void checkTaskSwitch(Integer taskswitch) throws ReStateException {
        if (taskswitch == null || taskswitch == 0) {
            throw new ReStateException("任务开关已关闭");
        }
    }
}
