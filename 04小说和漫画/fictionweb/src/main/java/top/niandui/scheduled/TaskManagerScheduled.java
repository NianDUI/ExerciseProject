package top.niandui.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.niandui.common.base.IBaseScheduled;
import top.niandui.dao.ITaskDao;
import top.niandui.model.vo.TaskListReturnVO;
import top.niandui.model.vo.TaskSearchVO;

import java.util.Date;
import java.util.List;

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
    // 触发表达式对象
    private final CronTrigger cronTrigger = new CronTrigger("0/10 * * * * ?");
    @Autowired
    private ITaskDao iTaskDao;
//    @Autowired
    private final ScheduledTaskRegistrar taskRegistrar = new ScheduledTaskRegistrar();

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
        return cronTrigger.nextExecutionTime(triggerContext);
    }

    @Override
    public boolean isEnable() {
        return false;
    }

    public void taskManager() throws Exception {
        List<TaskListReturnVO> list = iTaskDao.queryList(new TaskSearchVO());
        System.out.println("list = " + list);
    }

}
