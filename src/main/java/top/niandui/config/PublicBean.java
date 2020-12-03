package top.niandui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.dao.IBookDao;

/**
 * 基本bean
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 21:55
 */
@Configuration
@AutoConfigureOrder
//@AutoConfigureAfter({RedisUtil.class, ConfigInfo.class})
public class PublicBean {
    // 是否可以设置
    private static boolean isSetUp = true;
    public static ConfigInfo configInfo;
    public static RedisUtil redisUtil;
    public static IBookDao iBookDao;

    @Autowired
    public PublicBean(RedisUtil redisUtil, ConfigInfo configInfo, IBookDao iBookDao) {
        if (isSetUp) {
            synchronized (PublicBean.class) {
                if (isSetUp) {
                    isSetUp = false;
                    PublicBean.redisUtil = redisUtil;
                    PublicBean.configInfo = configInfo;
                    PublicBean.iBookDao = iBookDao;
                }
            }
        }
    }
}
