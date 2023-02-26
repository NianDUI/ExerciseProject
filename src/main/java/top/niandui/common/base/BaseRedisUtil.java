package top.niandui.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/7 11:27
 */
@Slf4j
public abstract class BaseRedisUtil<T> {
    protected final RedisTemplate<String, T> redisTemplate;

    public BaseRedisUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, T> getRedisTemplate() {
        return redisTemplate;
    }

    /**********************工具方法*********************/

    /**
     * 设置缓存key的过期时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取缓存key的过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断缓存key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存key
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 模糊查询key(scan)
     * 用于代替keys(单线程，阻塞)
     * 默认设置：
     * count：Long.MAX_VALUE
     *
     * @param pattern 查询的指定模式
     * @return 模糊查询出的key列表
     */
    public Set<String> scan(String pattern) {
        Set<String> keys = new HashSet<>();
        redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(KeyScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(bytes -> keys.add(new String(bytes)));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        });
        return keys;
    }

    // ============================T=============================

    /**
     * 获取普通缓存key的值
     *
     * @param key 键
     * @return 值
     */
    public T get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 以原子方式设置key为value并返回存储在key中的旧值
     *
     * @param key   键
     * @param value 新值
     * @return 旧值
     */
    public T getSet(String key, T value) {
        return key == null ? null : redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 向普通缓存key中放入元素
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    /**
     * 向普通缓存key中放入元素,并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * key不存在则设置值为value
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setNx(String key, T value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * key不存在则设置值为value并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false失败
     */
    public boolean setNx(String key, T value, long time) {
        try {
            if (time > 0) {
                return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
            }
            return setNx(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================HashMap=================================

    /**
     * 获取hash缓存key对应的所有键值(map)
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, T> hmGet(String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * 向hash缓存key中插入所有键值(map)
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmSet(String key, Map<String, T> map) {
        try {
            HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
            hashOperations.putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向hash缓存key中插入所有键值(map),并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmSet(String key, Map<String, T> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取Hash缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long hmSize(String key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    // ================================Hash=================================

    /**
     * 获取hash缓存key中对应键(item)的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public T hGet(String key, String item) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, item);
    }

    /**
     * 向hash缓存key中插入键(item)的值(value),如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, T value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向hash缓存key中插入键(item)的值(value),如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, T value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除hash缓存key中键(item)的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, String... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash缓存key中是否有该键(item)的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 新值
     */
    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 新值
     */
    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 获取set缓存key的值
     *
     * @param key 键
     * @return key所有元素
     */
    public Set<T> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断set缓存key中值(value)是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasValue(String key, T value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向set缓存key中放入元素
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sAdd(String key, T... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 向set缓存key中放入元素,并设置时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sAdd(String key, long time, T... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取set缓存key的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 移除set缓存key中值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRem(String key, T... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存key中[start,end]区间的元素
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return 区间的元素列表
     */
    public List<T> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取list缓存key的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取list缓存key中的索引为index值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 指定索引的值
     */
    public T lIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 向list缓存key中放入数据
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean lSet(String key, T value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向list缓存key中放入数据,并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true成功 false失败
     */
    public boolean lSet(String key, T value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向list缓存key中放入数据
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean lSet(String key, List<T> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向list缓存key中放入数据,并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true成功 false失败
     */
    public boolean lSet(String key, List<T> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置list缓存key中index索引处的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true成功 false失败
     */
    public boolean lSet(String key, long index, T value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除list缓存key中count个值为value的元素
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRem(String key, long count, T value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    // ============================zset=============================
    // 索引取值范额：0:第一个元素, 1:第二个元素, -1:最后一个元素, -2:倒数第二个元素

    /**
     * 获取zset缓存key中, 索引在[start,end]区间的元素, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 查询结果
     */
    public Set<T> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key 键
     * @param min 最小分数(score)值
     * @param max 最大分数(score)值
     * @return 查询结果
     */
    public Set<T> zRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中, 分数(score)在[min,max]区间, 中从offset位置开始count数量的元素, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key    键
     * @param min    最小分数(score)值
     * @param max    最大分数(score)值
     * @param offset 范围内索引(从0计算)offset处
     * @param count  count数量的元素(超出范围，返回范围内的)
     * @return 查询结果
     */
    public Set<T> zRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中, 索引在[start,end]区间的元素, 集合成员按 score 值递减(从大到小)来排列
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 查询结果
     */
    public Set<T> zRevRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递减(从大到小)来排列
     *
     * @param key 键
     * @param min 最小分数(score)值
     * @param max 最大分数(score)值
     * @return 查询结果
     */
    public Set<T> zRevRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中, 分数(score)在[min,max]区间, 中从offset位置开始count数量的元素, 集合成员按 score 值递减(从大到小)来排列
     *
     * @param key    键
     * @param min    最小分数(score)值
     * @param max    最大分数(score)值
     * @param offset 范围内索引(从0计算)offset处
     * @param count  count数量的元素(超出范围，返回范围内的)
     * @return 查询结果
     */
    public Set<T> zRevRangeByScore(String key, double min, double max, long offset, long count) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取zset缓存key中元素的个数
     *
     * @param key 键
     * @return 查询结果
     */
    public long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 向zset缓存key中插入值为value, 分数为score的元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 插入结果
     */
    public boolean zAdd(String key, T value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向zset缓存key中插入多个值为value, 分数为score的元素
     *
     * @param key    键
     * @param tuples 存放值(value)和分数(score)的元素set集合
     * @return 插入的数量
     */
    public long zAdd(String key, Set<ZSetOperations.TypedTuple<T>> tuples) {
        try {
            return redisTemplate.opsForZSet().add(key, tuples);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 移除zset缓存key中多个值为value的元素
     *
     * @param key    键
     * @param values 要移除的元素值
     * @return 移除的数量
     */
    public long zRem(String key, T... values) {
        try {
            return redisTemplate.opsForZSet().remove(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 移除zset缓存key中, 索引在[start,end]区间的元素, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 移除的数量
     */
    public long zRemRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().removeRange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 移除zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key 键
     * @param min 最小分数(score)值
     * @param max 最大分数(score)值
     * @return 移除的数量
     */
    public long zRemRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取zset缓存key中, 分数(score)在[min,max]区间元素的数量, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key 键
     * @param min 最小分数(score)值
     * @param max 最大分数(score)值
     * @return 分数区间元素的数量
     */
    public long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取zset缓存key中, 值为value的元素索引位置, 集合成员按 score 值递增(从小到大)来排列
     *
     * @param key   键
     * @param value 元素的值
     * @return 元素索引位置
     */
    public long zRank(String key, T value) {
        try {
            return redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取zset缓存key中, 值为value的元素索引位置, 集合成员按 score 值递减(从大到小)来排列
     *
     * @param key   键
     * @param value 元素的值
     * @return 元素索引位置
     */
    public long zRevRank(String key, T value) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 获取zset缓存key中, 值为value的元素分数
     *
     * @param key   键
     * @param value 元素的值
     * @return 元素分数
     */
    public double zScore(String key, T value) {
        try {
            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 对zset缓存key中, 值为value元素的分数(score)，进行增量或减量
     *
     * @param key   键
     * @param value 元素的值
     * @param delta 增量(+)或减量(-)
     * @return 修改后元素分数的值
     */
    public double zIncrScore(String key, T value, double delta) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, delta);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }
}
