package top.niandui.common.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: IBaseDao.java
 * @description: Dao层基接口
 * @time: 2020/3/22 16:37
 * @author: liyongda
 * @version: 1.0
 */
public interface IBaseDao<T> {
    // 新增
    int create(T t);
    // 新增多条
    int createBatch(List<T> list);
    // 修改
    int update(T t);
    // 删除
    int delete(@Param("id") String id);
    // 查询列表
    <P> List<T> queryList(P t);
    // 查询单条
    <P> T model(P id);
    // 查询下拉
    <R> List<R> option();
    // 重名校验
    <P> int checkName(P t);
}
