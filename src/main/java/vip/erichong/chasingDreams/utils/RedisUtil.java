package vip.erichong.chasingDreams.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author eric
 * 该工具类由 AI 生成，包括常见的 Redis 操作
 */
@Component
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置缓存过期时间失败, key: {}, time: {}", key, time, e);
            return false;
        }
    }

    /**
     * 根据 key 获取过期时间
     * @param key 键 不能为 null
     * @return 时间(秒) 返回 0 代表为永久有效
     */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("获取缓存过期时间失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 判断 key 是否存在
     * @param key 键
     * @return true 存在 false 不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("判断缓存存在失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(Arrays.asList(key));
                }
            }
        } catch (Exception e) {
            logger.error("删除缓存失败, keys: {}", Arrays.toString(key), e);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("获取缓存失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置缓存失败, key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time 要大于 0 如果 time 小于等于 0 将设置无限期
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置缓存失败, key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于 0)
     */
    public long incr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递增因子必须大于 0");
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            logger.error("递增操作失败, key: {}, delta: {}", key, delta, e);
            return 0;
        }
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于 0)
     */
    public long decr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递减因子必须大于 0");
            }
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            logger.error("递减操作失败, key: {}, delta: {}", key, delta, e);
            return 0;
        }
    }

    // ================================Hash=================================

    /**
     * HashGet
     * @param key 键 不能为 null
     * @param item 项 不能为 null
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            logger.error("获取哈希项失败, key: {}, item: {}", key, item, e);
            return null;
        }
    }

    /**
     * 获取 hashKey 对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            logger.error("获取哈希所有项失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("设置哈希所有项失败, key: {}, map: {}", key, map, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置哈希所有项失败, key: {}, map: {}, time: {}", key, map, time, e);
            return false;
        }
    }

    /**
     * 向一张 hash 表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error("设置哈希项失败, key: {}, item: {}, value: {}", key, item, value, e);
            return false;
        }
    }

    /**
     * 向一张 hash 表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的 hash 表有时间,这里将会替换原有的时间
     * @return true 成功 false 失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置哈希项失败, key: {}, item: {}, value: {}, time: {}", key, item, value, time, e);
            return false;
        }
    }

    /**
     * 删除 hash 表中的值
     * @param key 键 不能为 null
     * @param item 项 可以使多个 不能为 null
     */
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            logger.error("删除哈希项失败, key: {}, items: {}", key, Arrays.toString(item), e);
        }
    }

    /**
     * 判断 hash 表中是否有该项的值
     * @param key 键 不能为 null
     * @param item 项 不能为 null
     * @return true 存在 false 不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            logger.error("判断哈希项存在失败, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    /**
     * hash 递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于 0)
     */
    public double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            logger.error("哈希递增失败, key: {}, item: {}, by: {}", key, item, by, e);
            return 0;
        }
    }

    /**
     * hash 递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于 0)
     */
    public double hdecr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        } catch (Exception e) {
            logger.error("哈希递减失败, key: {}, item: {}, by: {}", key, item, by, e);
            return 0;
        }
    }

    // ============================set=============================

    /**
     * 根据 key 获取 Set 中的所有值
     * @param key 键
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("获取集合所有项失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 根据 value 从一个 set 中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false 不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("判断集合项存在失败, key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入 set 缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("添加集合项失败, key: {}, values: {}", key, Arrays.toString(values), e);
            return 0;
        }
    }

    /**
     * 将 set 数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            logger.error("添加集合项失败, key: {}, values: {}, time: {}", key, Arrays.toString(values), time, e);
            return 0;
        }
    }

    /**
     * 获取 set 缓存的长度
     * @param key 键
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("获取集合大小失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 移除值为 value 的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            logger.error("移除集合项失败, key: {}, values: {}", key, Arrays.toString(values), e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取 list 缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1 代表所有值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("获取列表项失败, key: {}, start: {}, end: {}", key, start, end, e);
            return null;
        }
    }

    /**
     * 获取 list 缓存的长度
     * @param key 键
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("获取列表大小失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取 list 中的值
     * @param key 键
     * @param index 索引 index>=0 时, 0 表头, 1 第二个元素,依次类推; index<0 时, -1,表尾, -2 倒数第二个元素,依次类推
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("获取列表指定索引项失败, key: {}, index: {}", key, index, e);
            return null;
        }
    }

    /**
     * 将 list 放入缓存
     * @param key 键
     * @param value 值
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("添加列表项失败, key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将 list 放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("添加列表项失败, key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 将 list 放入缓存
     * @param key 键
     * @param value 值
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error("添加列表所有项失败, key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 将 list 放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            logger.error("添加列表所有项失败, key: {}, value: {}, time: {}", key, value, time, e);
            return false;
        }
    }

    /**
     * 根据索引修改 list 中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("更新列表项失败, key: {}, index: {}, value: {}", key, index, value, e);
            return false;
        }
    }

    /**
     * 移除 N 个值为 value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            logger.error("移除列表项失败, key: {}, count: {}, value: {}", key, count, value, e);
            return 0;
        }
    }

    // =========================有序集合 sort set=========================

    /**
     * 向有序集合添加一个成员，或者更新已存在成员的分数
     * @param key 键
     * @param value 值
     * @param score 分数
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            logger.error("添加有序集合项失败, key: {}, value: {}, score: {}", key, value, score, e);
            return false;
        }
    }

    /**
     * 获取有序集合的成员数
     * @param key 键
     */
    public long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            logger.error("获取有序集合大小失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 计算在有序集合中指定区间分数的成员数
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     */
    public long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            logger.error("统计有序集合区间项数失败, key: {}, min: {}, max: {}", key, min, max, e);
            return 0;
        }
    }

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     */
    public Set<Object> zRevRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("获取有序集合区间项失败(倒序), key: {}, min: {}, max: {}", key, min, max, e);
            return null;
        }
    }

    /**
     * 返回有序集中指定分数区间内的成员，分数从低到高排序
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("获取有序集合区间项失败(正序), key: {}, min: {}, max: {}", key, min, max, e);
            return null;
        }
    }
}

