package top.niandui.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.niandui.common.base.IBaseScheduled;
import top.niandui.service.impl.VideoPlayServiceImpl;

import java.time.Instant;

@Slf4j
@Component
public class VideoCacheCleanupScheduled implements IBaseScheduled {
    private static final CronTrigger CRON_TRIGGER = new CronTrigger("0 0 * * * ?");

    @Autowired
    private VideoPlayServiceImpl videoPlayService;

    @Override
    public void run() {
        try {
            videoPlayService.cleanupExpiredCache();
        } catch (Exception e) {
            log.error("清理视频缓存失败", e);
        }
    }

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        return CRON_TRIGGER.nextExecution(triggerContext);
    }
}
