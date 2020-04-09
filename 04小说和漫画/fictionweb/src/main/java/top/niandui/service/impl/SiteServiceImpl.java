package top.niandui.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.model.IdNameModel;
import top.niandui.dao.IBookDao;
import top.niandui.dao.ISiteDao;
import top.niandui.model.Site;
import top.niandui.model.vo.SiteListReturnVO;
import top.niandui.model.vo.SiteSearchVO;
import top.niandui.service.ISiteService;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title SiteServiceImpl.java
 * @description 站点Service实现类
 * @time 2020/03/22 16:18:53
 */
@Service
public class SiteServiceImpl extends BaseServiceImpl implements ISiteService {
    @Autowired
    private ISiteDao iSiteDao;
    @Autowired
    private IBookDao iBookDao;

    @Override
    public void create(Site site) throws Exception {
        // 检查重名
        checkName(new IdNameModel(site.getSiteid(), site.getName()));
        iSiteDao.create(site);
    }

    @Override
    public void update(Site site) throws Exception {
        // 检查重名
        checkName(new IdNameModel(site.getSiteid(), site.getName()));
        iSiteDao.update(site);
    }

    @Override
    public void delete(String id) throws Exception {
        int count = iBookDao.querySiteBookCount(id);
        if (count > 0) {
            throw new ReStateException("站点存在书籍!");
        }
        iSiteDao.delete(id);
    }

    @Override
    public PageInfo<SiteListReturnVO> queryList(SiteSearchVO searchVO) throws Exception {
//        addDefaultSort(searchVO, "createtime", "DESC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<SiteListReturnVO>(iSiteDao.queryList(searchVO));
    }

    @Override
    public Site model(Long id) throws Exception {
        return (Site) iSiteDao.model(id);
    }

    @Override
    public List<IdNameModel> option() throws Exception {
        return iSiteDao.option();
    }

    @Override
    public void checkName(IdNameModel checkName) throws Exception {
        if (iSiteDao.checkName(checkName) > 0) {
            throw new RuntimeException("站点已存在");
        }
    }
}
