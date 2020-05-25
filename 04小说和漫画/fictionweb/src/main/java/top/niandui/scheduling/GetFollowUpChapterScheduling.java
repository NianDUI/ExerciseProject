package top.niandui.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.niandui.common.model.IdNameModel;
import top.niandui.service.IBookService;
import top.niandui.service.IChapterService;

import java.util.List;

/**
 * @Title: GetFollowUpChapterScheduling.java
 * @description: 获取后续章节定时任务
 * @time: 2020/5/23 13:51
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@Component
public class GetFollowUpChapterScheduling {
    @Autowired
    private IBookService iBookService;
    @Autowired
    private IChapterService iChapterService;

//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 16 0/4 * * ?")
    public void getFollowUpChapter() throws Exception {
        log.info("定时任务启动...");
        List<IdNameModel<Long>> option = iBookService.option();
        System.out.println("option = " + option);
        for (IdNameModel<Long> idNameModel : option) {
            String name = idNameModel.getName();
            log.info(String.format("获取 %s 后续章节...", name));
            try {
                iChapterService.getFollowUpChapter(idNameModel.getId());
                log.info(String.format("获取 %s 后续章节结束。", name));
            } catch (Exception e) {
                log.info(String.format("获取 %s 后续章节错误！\n %s", name, e.getMessage()));
                log.error(e.toString());
            }
        }
        log.info("定时任务结束。");
    }
}
