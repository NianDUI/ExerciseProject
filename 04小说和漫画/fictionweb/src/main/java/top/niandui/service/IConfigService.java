package top.niandui.service;

import com.github.pagehelper.PageInfo;
import top.niandui.common.model.IdNameModel;
import top.niandui.model.Config;
import top.niandui.model.vo.ConfigSearchVO;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title IConfigService.java
 * @description 配置Service
 * @time 2020/4/6 14:44
 */
public interface IConfigService {
    // 新增
    void create(Config config) throws Exception;

    // 修改
    void update(Config config) throws Exception;

    // 删除
    void delete(String id) throws Exception;

    // 查询列表
    PageInfo<Config> queryList(ConfigSearchVO searchVO) throws Exception;

    // 查询单条
    Config model(Long id) throws Exception;

    // 查询下拉
    List<IdNameModel<Long>> option() throws Exception;

    // 重名校验
    void checkName(IdNameModel<Long> checkName) throws Exception;
}