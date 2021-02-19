package top.niandui.dao;

import org.springframework.stereotype.Repository;
import top.niandui.common.base.IBaseDao;
import top.niandui.model.Site;

/**
 * @author 李永达
 * @version 1.0
 * @title ISiteDao.java
 * @description 站点
 * @time 2020/03/22 16:18:53
 */
@Repository
public interface ISiteDao<T> extends IBaseDao<T> {
    // 更新站点书籍配置
    void updateSiteBookConfig(Site site);
}