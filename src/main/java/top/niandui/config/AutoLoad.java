package top.niandui.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.dao.IBookDao;
import top.niandui.model.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.niandui.config.PublicConstant.BOOK_TASK_STATUS;

/**
 * 应用启动自动加载
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 23:22
 */
@Slf4j
@Component
public class AutoLoad implements ApplicationRunner {
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadBookTaskStatus();
    }

    private void loadBookTaskStatus() {
        log.info("==========>>>>>>>>>> 开始加载书籍任务状态至缓存...");
        try {
            List<Book> statusList = iBookDao.queryBookTaskstatusList();
            Map<String, Object> statusMap = new HashMap<>();
            statusList.forEach(item -> statusMap.put(item.getBookid() + "", item.getTaskstatus()));
            redisUtil.del(BOOK_TASK_STATUS);
            redisUtil.hmSet(BOOK_TASK_STATUS, statusMap);
        } catch (Exception e) {
            log.error("==========>>>>>>>>>> 加载书籍任务状态至缓存出错!", e);
        }
        log.info("==========>>>>>>>>>> 加载书籍任务状态至缓存完成!");
    }
}
