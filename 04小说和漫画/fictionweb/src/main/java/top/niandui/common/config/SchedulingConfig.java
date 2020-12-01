package top.niandui.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import top.niandui.common.base.IBaseScheduled;

/**
 * 定时任务配置
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/26 14:39
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {
    // 应用上下文
    private final ApplicationContext applicationContext;

    public SchedulingConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        String[] beanNames = applicationContext.getBeanNamesForType(IBaseScheduled.class);
        for (String beanName : beanNames) {
            try {
                IBaseScheduled scheduled = applicationContext.getBean(beanName, IBaseScheduled.class);
                if (scheduled.isEnable()) {
                    TriggerTask triggerTask = new TriggerTask(scheduled, scheduled);
                    taskRegistrar.addTriggerTask(triggerTask);
                }
            } catch (Exception e) {
                log.error(beanName + " 任务启动失败：" + e.getMessage(), e);
            }
        }
    }
}
