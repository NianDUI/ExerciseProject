package top.niandui.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.niandui.common.base.IBaseScheduled;
import top.niandui.common.model.IdNameModel;
import top.niandui.service.IBookService;
import top.niandui.service.IChapterService;

import java.util.Date;
import java.util.List;

/**
 * 获取后续章节定时任务
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/5/23 13:51
 */
@Slf4j
@Component
public class GetFollowUpChapterScheduled implements IBaseScheduled {
    @Autowired
    private IBookService iBookService;
    @Autowired
    private IChapterService iChapterService;
    // 触发表达式对象
    private final CronTrigger cronTrigger = new CronTrigger("0 16 0/6 * * ?");

    @Override
    public void run() {
        try {
            getFollowUpChapter();
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
        return true;
    }

    public void getFollowUpChapter() throws Exception {
        log.info("定时任务启动...");
        List<IdNameModel<Long>> option = iBookService.option();
        for (IdNameModel<Long> idNameModel : option) {
            String name = idNameModel.getName();
            log.info(String.format("获取 %s 后续章节...", name));
            try {
                iChapterService.getFollowUpChapter(idNameModel.getId());
                log.info(String.format("获取 %s 后续章节结束。", name));
            } catch (Exception e) {
                log.info(String.format("获取 %s 后续章节错误！：%s", name, e.getMessage()));
                log.error(e.getMessage(), e);
            }
        }
        log.info("定时任务结束。");
    }
}
