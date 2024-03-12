package top.niandui.scheduled;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.niandui.common.base.IBaseScheduled;
import top.niandui.common.uitls.redis.RedisLockUtil;
import top.niandui.dao.ITaskDao;
import top.niandui.model.vo.TaskListReturnVO;
import top.niandui.model.vo.TaskSearchVO;

import javax.xml.ws.Holder;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static top.niandui.common.base.BaseRedisLockUtil.LOCK;
import static top.niandui.config.PublicConstant.MINUTE_SECOND;

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
    // 锁key
    private static final String LOCK_KEY = LOCK + "task_manager";
    // 锁时间
    private static final long LOCK_TIME = MINUTE_SECOND * 11;
    @Autowired
    private ITaskDao iTaskDao;
    @Autowired
    private RedisLockUtil redisLockUtil;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run() {
        try {
            taskManager();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        return CRON_TRIGGER.nextExecution(triggerContext);
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    // 任务管理方法
    private void taskManager() throws Exception {
        // 尝试获取ip锁
        if (!redisLockUtil.tryLockIp(LOCK_KEY, LOCK_TIME)) {
            // 为获取锁
            return;
        }
        // 将任务映射放入删除任务映射
        RMV_TASK_MAP.putAll(TASK_MAP);
        // 任务映射清空
        TASK_MAP.clear();
        List<TaskListReturnVO> list = iTaskDao.queryList(new TaskSearchVO(1));
        for (TaskListReturnVO task : list) {
            Long taskid = task.getTaskid();
            // 从删除任务映射中，根具任务id获取Cron任务类
            CronTask cronTask = RMV_TASK_MAP.remove(taskid);
            if (cronTask == null) {
                // Cron任务类为null时，尝试进行加载
                Object bean = getClassObject(task);
                if (!(bean instanceof Runnable)) {
                    // bean 不是 Runnable 实现类(包含 bean 为 null)
                    continue;
                }
                // 获取到了任务类对象，创建Cron任务类
                cronTask = new CronTask((Runnable) bean, task.getCron());
                // 启动任务
                cronTask.setScheduledFuture(TASK_SCHEDULER.schedule(cronTask.getRunnable(), cronTask.getTriggerHolder().value));
            } else {
                // Cron任务类存在，尝试更新cron
                cronTask.setExpression(task.getCron());
            }
            // 将还需要继续执行的任务放入任务映射
            TASK_MAP.put(taskid, cronTask);
        }
        // 对不需要继续执行的任务，停止
        RMV_TASK_MAP.forEach((taskid, cronTask) -> cronTask.getScheduledFuture().cancel(true));
        // 清空删除任务映射
        RMV_TASK_MAP.clear();
    }

    // 获取类对象
    private Object getClassObject(TaskListReturnVO task) {
        try {
            if (task.getLoadmanner() == 0) {
                // 通过反射创建
                return Class.forName(task.getClasspath()).getDeclaredConstructor().newInstance();
            } else if (task.getLoadmanner() == 1) {
                // 从IOC容器中获取
                return applicationContext.getBean(Class.forName(task.getClasspath()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    // Cron任务类
    private static class CronTask extends org.springframework.scheduling.config.Task {
        private String expression;
        private Trigger trigger;
        // 使用Holder是为了防止，expression修改后不生效
        @Getter
        private final Holder<Trigger> triggerHolder = new Holder<>(
                triggerContext -> trigger.nextExecution(triggerContext));
        @Setter
        @Getter
        private ScheduledFuture<?> scheduledFuture;

        public CronTask(Runnable runnable, String expression) {
            super(runnable);
            this.expression = expression;
            this.trigger = new CronTrigger(expression);
        }

        // 设置更新expression
        public void setExpression(String expression) {
            if (!Objects.equals(this.expression, expression)) {
                this.expression = expression;
                this.trigger = new CronTrigger(expression);
            }
        }
    }
}
