package top.niandui.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步配置 {@link TaskExecutionAutoConfiguration Auto-configuration}
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/11/14 16:42
 */
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class AsyncConfig {
    // 获取数据任务执行器Bean的名字
    public static final String GET_BOOK_TASK_EXECUTOR_BEAN_NAME = "getBookTaskExecutor";

    /**
     * 注入默认的线程池任务执行器
     * <br>用于防止注入自定义时{@link TaskExecutionAutoConfiguration TaskExecutionAutoConfiguration.applicationTaskExecutor()}不生效。
     *
     * @param builder 任务执行器生成器
     * @return 默认的线程池任务执行器
     */
    @Lazy
    @Primary // 指示当多个候选者有资格自动装配单值依赖项时，应该优先考虑一个的 bean
    @Bean({TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME, AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME})
//    @ConditionalOnMissingBean({Executor.class})
    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
        return builder.build();
    }

    /**
     * 获取书籍线程池任务执行器
     *
     * @param properties              任务执行属性
     * @param taskExecutorCustomizers 任务执行器定制器
     * @param taskDecorator           任务装饰器
     * @return 异步任务执行器
     */
    @Lazy
    @Bean(GET_BOOK_TASK_EXECUTOR_BEAN_NAME)
    public ThreadPoolTaskExecutor getBookTaskExecutor(TaskExecutionProperties properties
            , ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers
            , ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity());
        int coreSize = pool.getCoreSize();
        if (coreSize > 1) {
            // 如果核心量大于1，对其除以2
            coreSize = coreSize / 2;
        }
        builder = builder.corePoolSize(coreSize);
        builder = builder.maxPoolSize(coreSize);
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix("get-book-task-");
        builder = builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
        builder = builder.taskDecorator(taskDecorator.getIfUnique());
        return builder.build();
    }
}
