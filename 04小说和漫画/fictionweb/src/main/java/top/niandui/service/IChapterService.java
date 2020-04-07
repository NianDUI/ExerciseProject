package top.niandui.service;

import com.github.pagehelper.PageInfo;
import top.niandui.common.model.IdNameModel;
import top.niandui.model.Chapter;
import top.niandui.model.vo.ChapterInfoReturnVO;
import top.niandui.model.vo.ChapterListReturnVO;
import top.niandui.model.vo.ChapterSearchVO;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title IChapterService.java
 * @description 章节Service
 * @time 2020/4/6 17:22
 */
public interface IChapterService {
    // 新增
    void create(Chapter chapter) throws Exception;

    // 修改
    void update(Chapter chapter) throws Exception;

    // 删除
    void delete(String id) throws Exception;

    // 查询列表
    PageInfo<ChapterListReturnVO> queryList(ChapterSearchVO searchVO) throws Exception;

    // 查询单条
    Chapter model(Long id) throws Exception;

    // 查询下拉
    List<IdNameModel> option() throws Exception;

    // 重名校验
    void checkName(IdNameModel checkName) throws Exception;

    // 获取章节
    void reacquireChapter(Long id) throws Exception;

    // 获取后续章节
    void getFollowUpChapter(Long id) throws Exception;

    // 查询章节信息
    ChapterInfoReturnVO queryChapterInfo(Long id) throws Exception;
}