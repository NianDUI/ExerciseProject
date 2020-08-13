package top.niandui.utils;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author liyongda
 * @description redis byte(二进制) 工具类
 * @date 2019/12/30 16:02
 */
@Component
public class RedisUtilByte {
	private static class ByteRedisSerializer implements RedisSerializer<byte[]> {
		@Override
		public byte[] serialize(byte[] bytes) throws SerializationException {
			return bytes;
		}

		@Override
		public byte[] deserialize(byte[] bytes) throws SerializationException {
			return bytes;
		}
	}

	private final RedisTemplate<String, byte[]> redisTemplateByte;

	public RedisUtilByte(RedisConnectionFactory factory) {
		RedisTemplate<String, byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		ByteRedisSerializer byteRedisSerializer = new ByteRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(RedisSerializer.string());
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(RedisSerializer.string());
		// value序列化方式采用 自定义byte数组的序列化方式
		template.setValueSerializer(byteRedisSerializer);
		// hash的value序列化方式采用 自定义byte数组的序列化方式
		template.setHashValueSerializer(byteRedisSerializer);
		template.afterPropertiesSet();
		redisTemplateByte = template;
	}

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
				redisTemplateByte.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
		return redisTemplateByte.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断缓存key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplateByte.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存key
	 *
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplateByte.delete(key[0]);
			} else {
				redisTemplateByte.delete(CollectionUtils.arrayToList(key));
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
		Set<String> keys = new HashSet<String>();
		redisTemplateByte.execute((RedisConnection connection) -> {
			try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
				while (cursor.hasNext()) {
					keys.add(new String(cursor.next()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
		return keys;
	}

	// ============================byte[]=============================

	/**
	 * 获取普通缓存key的值
	 *
	 * @param key 键
	 * @return 值
	 */
	public byte[] get(String key) {
		return key == null ? null : redisTemplateByte.opsForValue().get(key);
	}

	/**
	 * 以原子方式设置key为value并返回存储在key中的旧值
	 *
	 * @param key   键
	 * @param value 新值
	 * @return 旧值
	 */
	public byte[] getSet(String key, byte[] value) {
		try {
			return redisTemplateByte.opsForValue().getAndSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 向普通缓存key中放入元素
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key, byte[] value) {
		try {
			redisTemplateByte.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public boolean set(String key, byte[] value, long time) {
		try {
			if (time > 0) {
				redisTemplateByte.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
		return redisTemplateByte.opsForValue().increment(key, delta);
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
		return redisTemplateByte.opsForValue().increment(key, -delta);
	}

	// ================================HashMap=================================

	/**
	 * 获取hash缓存key对应的所有键值(map)
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<String, byte[]> hmGet(String key) {
		HashOperations<String, String, byte[]> hashOperations = redisTemplateByte.opsForHash();
		return hashOperations.entries(key);
	}

	/**
	 * 向hash缓存key中插入所有键值(map)
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmSet(String key, Map<String, byte[]> map) {
		try {
			HashOperations<String, String, byte[]> hashOperations = redisTemplateByte.opsForHash();
			hashOperations.putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public boolean hmSet(String key, Map<String, byte[]> map, long time) {
		try {
			HashOperations<String, String, byte[]> hashOperations = redisTemplateByte.opsForHash();
			hashOperations.putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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
	public byte[] hGet(String key, String item) {
		HashOperations<String, String, byte[]> hashOperations = redisTemplateByte.opsForHash();
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
	public boolean hSet(String key, String item, byte[] value) {
		try {
			redisTemplateByte.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public boolean hSet(String key, String item, byte[] value, long time) {
		try {
			redisTemplateByte.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
		redisTemplateByte.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash缓存key中是否有该键(item)的值
	 *
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item) {
		return redisTemplateByte.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public double hIncr(String key, String item, double by) {
		return redisTemplateByte.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public double hDecr(String key, String item, double by) {
		return redisTemplateByte.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================

	/**
	 * 获取set缓存key的值
	 *
	 * @param key 键
	 * @return
	 */
	public Set<byte[]> sGet(String key) {
		try {
			return redisTemplateByte.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取set缓存key的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long sGetSize(String key) {
		try {
			return redisTemplateByte.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 向set缓存key中放入元素
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, byte[]... values) {
		try {
			return redisTemplateByte.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
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
	public long sSet(String key, long time, byte[]... values) {
		try {
			Long count = redisTemplateByte.opsForSet().add(key, values);
			if (time > 0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 判断set缓存key中值(value)是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, byte[] value) {
		try {
			return redisTemplateByte.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除set缓存key中值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long sRemove(String key, byte[]... values) {
		try {
			Long count = redisTemplateByte.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return
	 */
	public List<byte[]> lGet(String key, long start, long end) {
		try {
			return redisTemplateByte.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存key的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long lGetSize(String key) {
		try {
			return redisTemplateByte.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取list缓存key中的索引为index值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public byte[] lGetIndex(String key, long index) {
		try {
			return redisTemplateByte.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 向list缓存key中放入数据
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, byte[] value) {
		try {
			redisTemplateByte.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向list缓存key中放入数据,并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, byte[] value, long time) {
		try {
			redisTemplateByte.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向list缓存key中放入数据
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, List<byte[]> value) {
		try {
			redisTemplateByte.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向list缓存key中放入数据,并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<byte[]> value, long time) {
		try {
			redisTemplateByte.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置list缓存key中index索引处的值
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lSetIndex(String key, long index, byte[] value) {
		try {
			redisTemplateByte.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public long lRemove(String key, long count, byte[] value) {
		try {
			Long remove = redisTemplateByte.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @return		查询结果
	 */
	public Set<byte[]> zRange(String key, long start, long end) {
		try {
			return redisTemplateByte.opsForZSet().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key	键
	 * @param min	最小分数(score)值
	 * @param max	最大分数(score)值
	 * @return		查询结果
	 */
	public Set<byte[]> zRangeByScore(String key, double min, double max) {
		try {
			return redisTemplateByte.opsForZSet().rangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中, 分数(score)在[min,max]区间, 中从offset位置开始count数量的元素, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key		键
	 * @param min		最小分数(score)值
	 * @param max		最大分数(score)值
	 * @param offset	范围内索引(从0计算)offset处
	 * @param count		count数量的元素(超出范围，返回范围内的)
	 * @return			查询结果
	 */
	public Set<byte[]> zRangeByScore(String key, double min, double max, long offset, long count) {
		try {
			return redisTemplateByte.opsForZSet().rangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中, 索引在[start,end]区间的元素, 集合成员按 score 值递减(从大到小)来排列
	 *
	 * @param key   键
	 * @param start 开始索引
	 * @param end   结束索引
	 * @return		查询结果
	 */
	public Set<byte[]> zRevRange(String key, long start, long end) {
		try {
			return redisTemplateByte.opsForZSet().reverseRange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递减(从大到小)来排列
	 *
	 * @param key	键
	 * @param min	最小分数(score)值
	 * @param max	最大分数(score)值
	 * @return		查询结果
	 */
	public Set<byte[]> zRevRangeByScore(String key, double min, double max) {
		try {
			return redisTemplateByte.opsForZSet().reverseRangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中, 分数(score)在[min,max]区间, 中从offset位置开始count数量的元素, 集合成员按 score 值递减(从大到小)来排列
	 *
	 * @param key		键
	 * @param min		最小分数(score)值
	 * @param max		最大分数(score)值
	 * @param offset	范围内索引(从0计算)offset处
	 * @param count		count数量的元素(超出范围，返回范围内的)
	 * @return			查询结果
	 */
	public Set<byte[]> zRevRangeByScore(String key, double min, double max, long offset, long count) {
		try {
			return redisTemplateByte.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取zset缓存key中元素的个数
	 *
	 * @param key 键
	 * @return	  查询结果
	 */
	public long zCard(String key) {
		try {
			return redisTemplateByte.opsForZSet().zCard(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 向zset缓存key中插入值为value, 分数为score的元素
	 *
	 * @param key	键
	 * @param value	值
	 * @param score	分数
	 * @return		插入结果
	 */
	public boolean zAdd(String key, byte[] value, double score) {
		try {
			return redisTemplateByte.opsForZSet().add(key, value, score);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向zset缓存key中插入多个值为value, 分数为score的元素
	 *
	 * @param key		键
	 * @param tuples	存放值(value)和分数(score)的元素set集合
	 * @return			插入的数量
	 */
	public long zAdd(String key, Set<ZSetOperations.TypedTuple<byte[]>> tuples) {
		try {
			return redisTemplateByte.opsForZSet().add(key, tuples);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 移除zset缓存key中多个值为value的元素
	 *
	 * @param key		键
	 * @param values	要移除的元素值
	 * @return			移除的数量
	 */
	public long zRemove(String key, byte[]... values) {
		try {
			return redisTemplateByte.opsForZSet().remove(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 移除zset缓存key中, 索引在[start,end]区间的元素, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key	键
	 * @param start	开始索引
	 * @param end	结束索引
	 * @return		移除的数量
	 */
	public long zRemRange(String key, long start, long end) {
		try {
			return redisTemplateByte.opsForZSet().removeRange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 移除zset缓存key中, 分数(score)在[min,max]区间的元素, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key	键
	 * @param min	最小分数(score)值
	 * @param max	最大分数(score)值
	 * @return		移除的数量
	 */
	public long zRemRangeByScore(String key, double min, double max) {
		try {
			return redisTemplateByte.opsForZSet().removeRangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 获取zset缓存key中, 分数(score)在[min,max]区间元素的数量, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key	键
	 * @param min	最小分数(score)值
	 * @param max	最大分数(score)值
	 * @return		分数区间元素的数量
	 */
	public long zCount(String key, double min, double max) {
		try {
			return redisTemplateByte.opsForZSet().count(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 获取zset缓存key中, 值为value的元素索引位置, 集合成员按 score 值递增(从小到大)来排列
	 *
	 * @param key	键
	 * @param value	元素的值
	 * @return		元素索引位置
	 */
	public long zRank(String key, byte[] value) {
		try {
			return redisTemplateByte.opsForZSet().rank(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 获取zset缓存key中, 值为value的元素索引位置, 集合成员按 score 值递减(从大到小)来排列
	 *
	 * @param key	键
	 * @param value	元素的值
	 * @return		元素索引位置
	 */
	public long zRevRank(String key, byte[] value) {
		try {
			return redisTemplateByte.opsForZSet().reverseRank(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}

	/**
	 * 获取zset缓存key中, 值为value的元素分数
	 *
	 * @param key	键
	 * @param value	元素的值
	 * @return		元素分数
	 */
	public double zScore(String key, byte[] value) {
		try {
			return redisTemplateByte.opsForZSet().score(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 对zset缓存key中, 值为value元素的分数(score)，进行增量或减量
	 *
	 * @param key	键
	 * @param value	元素的值
	 * @param delta	增量(+)或减量(-)
	 * @return		修改后元素分数的值
	 */
	public double zIncrScore(String key, byte[] value, double delta) {
		try {
			return redisTemplateByte.opsForZSet().incrementScore(key, value, delta);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}