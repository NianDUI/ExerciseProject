package top.niandui.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.niandui.common.base.BaseServiceImpl;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.model.IdNameModel;
import top.niandui.dao.IConfigDao;
import top.niandui.model.Config;
import top.niandui.model.vo.ConfigSearchVO;
import top.niandui.service.IConfigService;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title ConfigServiceImpl.java
 * @description 配置Service实现类
 * @time 2020/4/6 14:46
 */
@Service
public class ConfigServiceImpl extends BaseServiceImpl implements IConfigService {
    @Autowired
    private IConfigDao iConfigDao;

    @Override
    public void create(Config config) throws Exception {
        // 检查重名
        checkName(new IdNameModel(config.getConfigid(), config.getName()));
        iConfigDao.create(config);
    }

    @Override
    public void update(Config config) throws Exception {
        // 检查重名
        checkName(new IdNameModel(config.getConfigid(), config.getName()));
        iConfigDao.update(config);
    }

    @Override
    public void delete(String id) throws Exception {
        long count = iConfigDao.queryConfigUseCount(id);
        if (count > 0) {
            throw new ReStateException("该配置已被使用!");
        }
        iConfigDao.delete(id);
    }

    @Override
    public PageInfo<Config> queryList(ConfigSearchVO searchVO) throws Exception {
//        addDefaultSort(searchVO, "createtime", "DESC");
        PageHelper.startPage(searchVO.getPageNum(), searchVO.getPageSize(), getOrder(searchVO));
        return new PageInfo<Config>(iConfigDao.queryList(searchVO));
    }

    @Override
    public Config model(Long id) throws Exception {
        return (Config) iConfigDao.model(id);
    }

    @Override
    public List<IdNameModel> option() throws Exception {
        return iConfigDao.option();
    }

    @Override
    public void checkName(IdNameModel checkName) throws Exception {
        if (iConfigDao.checkName(checkName) > 0) {
            throw new RuntimeException("配置已存在");
        }
    }
}
