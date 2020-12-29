package top.niandui.config;

/**
 * 公共常量
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 21:41
 */
public class PublicConstant {
    // 文件路径和md5映射key
    public static final String PATH_MAPPING_P_M = "path_mapping:p_m";
    public static final String PATH_MAPPING_M_P = "path_mapping:m_p";

    // 书籍任务状态：0无，1重新获取全部，2获取后续章节，3重新获取单章
    public static final String BOOK_TASK_STATUS = "book_task_status";

    // 书籍代理信息缓存key
    public static final String BOOK_PROXY = "book_proxy:";

    // 1分钟的秒数
    public static final long MINUTE_SECOND = 60;
    // 1小时的秒数
    public static final long HOUR_SECOND = MINUTE_SECOND * 60;
    // 1天的秒数
    public static final long DAY_SECOND = HOUR_SECOND * 24;

}
