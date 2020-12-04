package top.niandui.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.niandui.common.model.IdNameModel;
import top.niandui.dao.IBookDao;
import top.niandui.service.IChapterService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 获取后续章节定时任务
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/5/23 13:51
 */
@Slf4j
@Component
public class GetFollowUpChapterTask implements Runnable {
    // 下拉书籍参数
    private static final Map PARAMS = Collections.singletonMap("taskswitch", 1);
    // 是否启用
    private final boolean enable = true;
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private IChapterService iChapterService;

    @Override
    public void run() {
        if (enable) {
            try {
                getFollowUpChapter();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void getFollowUpChapter() throws Exception {
        log.info("定时任务启动...");
        List<IdNameModel<Long>> option = iBookDao.option(PARAMS);
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
