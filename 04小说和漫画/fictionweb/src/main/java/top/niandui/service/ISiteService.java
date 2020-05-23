package top.niandui.service;

import com.github.pagehelper.PageInfo;
import top.niandui.common.model.IdNameModel;
import top.niandui.model.Site;
import top.niandui.model.vo.SiteListReturnVO;
import top.niandui.model.vo.SiteSearchVO;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title ISiteService.java
 * @description 站点Service
 * @time 2020/03/22 16:18:53
 */
public interface ISiteService {
    // 新增
    void create(Site site) throws Exception;

    // 修改
    void update(Site site) throws Exception;

    // 删除
    void delete(String id) throws Exception;

    // 查询列表
    PageInfo<SiteListReturnVO> queryList(SiteSearchVO searchVO) throws Exception;

    // 查询单条
    Site model(Long id) throws Exception;

    // 查询下拉
    List<IdNameModel<Long>> option() throws Exception;

    // 重名校验
    void checkName(IdNameModel<Long> checkName) throws Exception;
}