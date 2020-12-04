package top.niandui.scheduled;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.niandui.common.base.IBaseScheduled;
import top.niandui.dao.ITaskDao;
import top.niandui.model.vo.TaskListReturnVO;
import top.niandui.model.vo.TaskSearchVO;

import javax.xml.ws.Holder;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 任务管理器的定时任务
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/1 10:56
 */
@Slf4j
@Component
public class TaskManagerScheduled implements IBaseScheduled {
    // 本地执行器
    private static final ScheduledExecutorService LOCAL_EXECUTOR = new ScheduledThreadPoolExecutor(10);
    // 并发任务调用器
    private static final ConcurrentTaskScheduler TASK_SCHEDULER = new ConcurrentTaskScheduler(LOCAL_EXECUTOR);
    // 任务映射
    private static final Map<Long, CronTask> TASK_MAP = new HashMap<>();
    // 删除任务映射
    private static final Map<Long, CronTask> RMV_TASK_MAP = new HashMap<>();
    // 触发表达式对象
    private static final CronTrigger CRON_TRIGGER = new CronTrigger("0 0/10 * * * ?");
    @Autowired
    private ITaskDao iTaskDao;

    @Override
    public void run() {
        try {
            taskManager();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        return CRON_TRIGGER.nextExecutionTime(triggerContext);
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    // 任务管理方法
    private void taskManager() throws Exception {
        RMV_TASK_MAP.putAll(TASK_MAP);
        TASK_MAP.clear();
        List<TaskListReturnVO> list = iTaskDao.queryList(new TaskSearchVO(1));
        for (TaskListReturnVO task : list) {
            Long taskid = task.getTaskid();
            CronTask cronTask = RMV_TASK_MAP.remove(taskid);
            if (cronTask == null) {
                cronTask = new CronTask((Runnable) Class.forName(task.getClasspath()).newInstance(), task.getCron());
                cronTask.setScheduledFuture(TASK_SCHEDULER.schedule(cronTask.getRunnable(), cronTask.getTriggerHolder().value));
            } else {
                cronTask.setExpression(task.getCron());
            }
            TASK_MAP.put(taskid, cronTask);
        }
        RMV_TASK_MAP.forEach((taskid, cronTask) -> cronTask.getScheduledFuture().cancel(true));
        RMV_TASK_MAP.clear();
    }

    // Cron任务类
    private static class CronTask extends org.springframework.scheduling.config.Task {
        private String expression;
        private Trigger trigger;
        @Getter
        private final Holder<Trigger> triggerHolder = new Holder<>(
                triggerContext -> trigger.nextExecutionTime(triggerContext));
        @Setter
        @Getter
        private ScheduledFuture<?> scheduledFuture;

        public CronTask(Runnable runnable, String expression) {
            super(runnable);
            this.expression = expression;
            this.trigger = new CronTrigger(expression);
        }

        public void setExpression(String expression) {
            if (!Objects.equals(this.expression, expression)) {
                this.expression = expression;
                this.trigger = new CronTrigger(expression);
            }
        }
    }
}
