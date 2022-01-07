package com.cl.common.utils;

import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis工具类
 *
 * @author: CarterCL
 * @date: 2022/1/1 14:50
 * @version: V1.0
 */
public final class RedisUtils {

    //region 常量

    private static final String DEFAULT_HOST = "127.0.0.1";

    private static final int DEFAULT_PORT = 6379;

    private static final int DEFAULT_DB_INDEX = 0;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10L);

    private static final String OK = "OK";

    //endregion

    private static RedisClient redisClient;

    private static StatefulRedisConnection<String, String> connection;

    private static boolean isInit = false;

    // region 字符串

    /**
     * set
     *
     * @param key   key
     * @param value value
     */
    public static void set(String key, String value) {
        getCommands().set(key, value);
    }

    /**
     * get
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        return getCommands().get(key);
    }

    /**
     * setEx
     *
     * @param key     key
     * @param value   value
     * @param seconds 过期时间(秒)
     */
    public static void setEx(String key, String value, Long seconds) {
        getCommands().setex(key, seconds, value);
    }

    /**
     * pSetEx
     *
     * @param key          key
     * @param value        value
     * @param milliseconds 过期时间(毫秒)
     */
    public static void pSetEx(String key, String value, Long milliseconds) {
        getCommands().psetex(key, milliseconds, value);
    }

    /**
     * setNx
     *
     * @param key   key
     * @param value value
     * @return set成功true，失败(key已存在)false
     */
    public static Boolean setNx(String key, String value) {
        return getCommands().setnx(key, value);
    }

    /**
     * setNx with expire
     *
     * @param key   key
     * @param value value
     * @return set成功true，失败(key已存在)false
     */
    public static Boolean setNxWithExpire(String key, String value, Duration timeout) {
        return OK.equals(getCommands().set(key, value, SetArgs.Builder.nx().ex(timeout)));
    }

    // endregion

    //region 列表

    /**
     * lPush
     *
     * @param key    key
     * @param values values
     * @return length
     */
    public static Long lPush(String key, String... values) {
        return getCommands().lpush(key, values);
    }

    /**
     * rPush
     *
     * @param key    key
     * @param values values
     * @return length
     */
    public static Long rPush(String key, String... values) {
        return getCommands().rpush(key, values);
    }

    /**
     * lPop
     *
     * @param key key
     * @return value
     */
    public static String lPop(String key) {
        return getCommands().lpop(key);
    }

    /**
     * lPop
     *
     * @param key   key
     * @param count count
     * @return values
     */
    public static List<String> lPop(String key, Integer count) {
        return getCommands().lpop(key, count);
    }

    /**
     * rPop
     *
     * @param key key
     * @return value
     */
    public static String rPop(String key) {
        return getCommands().rpop(key);
    }

    /**
     * rPop
     *
     * @param key   key
     * @param count count
     * @return values
     */
    public static List<String> rPop(String key, Integer count) {
        return getCommands().rpop(key, count);
    }

    //endregion

    //region 集合

    /**
     * sAdd
     *
     * @param key     key
     * @param members members
     * @return count
     */
    public Long sAdd(String key, String... members) {
        return getCommands().sadd(key, members);
    }

    /**
     * sMembers
     *
     * @param key key
     * @return members
     */
    public Set<String> sMembers(String key) {
        return getCommands().smembers(key);
    }

    //endregion

    //region 有序集合

    /**
     * zAdd
     *
     * @param key   key
     * @param score score
     * @param value value
     * @return count
     */
    public static Long zAdd(String key, double score, String value) {

        return getCommands().zadd(key, score, value);
    }

    /**
     * zRange
     *
     * @param key        key
     * @param startIndex 起始索引
     * @param stopIndex  结束索引
     * @return list
     */
    public static List<String> zRange(String key, int startIndex, int stopIndex) {
        return getCommands().zrange(key, startIndex, stopIndex);
    }

    /**
     * zRange withScores
     *
     * @param key        key
     * @param startIndex 起始索引
     * @param stopIndex  结束索引
     * @return list
     */
    public static List<ScoredValue<String>> zRangeWithScores(String key, int startIndex, int stopIndex) {
        return getCommands().zrangeWithScores(key, startIndex, stopIndex);
    }


    /**
     * zRangeByScore
     *
     * @param key        key
     * @param min        最小值
     * @param max        最大值
     * @param includeMin 是否包含最小值
     * @param includeMax 是否包含最大值
     * @return list
     */
    public static List<String> zRangeByScore(String key, double min, double max, boolean includeMin, boolean includeMax) {
        Range.Boundary<Double> minBoundary = includeMin ? Range.Boundary.including(min) : Range.Boundary.excluding(min);
        Range.Boundary<Double> maxBoundary = includeMax ? Range.Boundary.including(max) : Range.Boundary.excluding(max);
        return getCommands().zrangebyscore(key, Range.from(minBoundary, maxBoundary));
    }

    /**
     * zRangeByScore withScores
     *
     * @param key        key
     * @param min        最小值
     * @param max        最大值
     * @param includeMin 是否包含最小值
     * @param includeMax 是否包含最大值
     * @return list
     */
    public static List<ScoredValue<String>> zRangeByScoreWithScores(String key, double min, double max, boolean includeMin, boolean includeMax) {
        Range.Boundary<Double> minBoundary = includeMin ? Range.Boundary.including(min) : Range.Boundary.excluding(min);
        Range.Boundary<Double> maxBoundary = includeMax ? Range.Boundary.including(max) : Range.Boundary.excluding(max);
        return getCommands().zrangebyscoreWithScores(key, Range.from(minBoundary, maxBoundary));
    }


    //endregion

    //region 哈希

    /**
     * hSet
     *
     * @param key   key
     * @param field field
     * @param value value
     */
    public static void hSet(String key, String field, String value) {
        getCommands().hset(key, field, value);
    }

    /**
     * hSet
     *
     * @param key key
     * @param map field->value map
     * @return count
     */
    public static Long hSet(String key, Map<String, String> map) {
        return getCommands().hset(key, map);
    }

    /**
     * hGet
     *
     * @param key   key
     * @param field field
     * @return value
     */
    public static String hGet(String key, String field) {
        return getCommands().hget(key, field);
    }

    /**
     * hGetAll
     *
     * @param key key
     * @return map
     */
    public static Map<String, String> hGetAll(String key) {
        return getCommands().hgetall(key);
    }

    /**
     * hDel
     *
     * @param key    key
     * @param fields fields
     * @return count
     */
    public static Long hDel(String key, String... fields) {
        return getCommands().hdel(key, fields);
    }

    //endregion

    //region 常用命令

    /**
     * incr
     *
     * @param key key
     * @return value
     */
    public static Long incr(String key) {
        return getCommands().incr(key);
    }

    /**
     * incr
     *
     * @param key    key
     * @param amount amount
     * @return value
     */
    public static Long incr(String key, Long amount) {
        return getCommands().incrby(key, amount);
    }

    /**
     * decr
     *
     * @param key key
     * @return value
     */
    public static Long decr(String key) {
        return getCommands().decr(key);
    }

    /**
     * decr
     *
     * @param key    key
     * @param amount amount
     * @return value
     */
    public static Long decr(String key, Long amount) {
        return getCommands().decrby(key, amount);
    }

    /**
     * 执行lua脚本
     *
     * @param luaScript lua脚本
     * @param keys      keys
     * @param args      args
     * @param clazz     返回值类型
     * @return 结果
     */
    public static <T> T eval(String luaScript, String[] keys, String[] args, Class<T> clazz) {

        ScriptOutputType scriptOutputType;
        if (Integer.class.equals(clazz) || Long.class.equals(clazz)) {
            scriptOutputType = ScriptOutputType.INTEGER;
        } else if (Boolean.class.equals(clazz)) {
            scriptOutputType = ScriptOutputType.BOOLEAN;
        } else if (String.class.equals(clazz)) {
            scriptOutputType = ScriptOutputType.VALUE;
        } else {
            throw new UnsupportedOperationException("");
        }

        return getCommands().eval(luaScript, scriptOutputType, keys, args);
    }
    //endregion

    //region 通用命令

    /**
     * del
     *
     * @param keys keys
     * @return count
     */
    public static Long del(String... keys) {
        return getCommands().del(keys);
    }

    /**
     * unlink
     *
     * @param keys keys
     * @return count
     */
    public static Long unlink(String... keys) {
        return getCommands().unlink(keys);
    }

    /**
     * 设置ttl(秒)
     *
     * @param key     key
     * @param seconds seconds
     */
    public static void expire(String key, Long seconds) {
        getCommands().expire(key, seconds);
    }

    /**
     * 设置ttl(毫秒)
     *
     * @param key          key
     * @param milliseconds milliseconds
     */
    public static void pExpire(String key, Long milliseconds) {
        getCommands().pexpire(key, milliseconds);
    }

    //endregion

    //region 初始化连接

    /**
     * 初始化
     */
    public static void init() {
        init(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * 初始化
     *
     * @param host 地址
     * @param port 端口
     */
    public static void init(String host, Integer port) {
        init(host, port, DEFAULT_DB_INDEX);
    }

    /**
     * 初始化
     *
     * @param host    地址
     * @param port    端口
     * @param dbIndex 数据库
     */
    public static void init(String host, Integer port, Integer dbIndex) {
        init(host, port, dbIndex, null);
    }

    /**
     * 初始化
     *
     * @param host     地址
     * @param port     端口
     * @param dbIndex  数据库
     * @param password 密码
     */
    public static void init(String host, Integer port, Integer dbIndex, CharSequence password) {
        init(host, port, dbIndex, password, DEFAULT_TIMEOUT);
    }

    /**
     * 初始化
     *
     * @param host     地址
     * @param port     端口
     * @param dbIndex  db
     * @param password 密码
     * @param timeout  超时时间
     */
    public static void init(String host, Integer port, Integer dbIndex, CharSequence password, Duration timeout) {
        RedisURI redisUri = RedisURI.builder()
                .withHost((host == null || host.length() == 0) ? DEFAULT_HOST : host)
                .withPort(port == null ? DEFAULT_PORT : port)
                .withDatabase(dbIndex == null ? DEFAULT_DB_INDEX : dbIndex)
                .build();
        if (password != null && password.length() > 0) {
            redisUri.setPassword(password);
        }

        if (timeout != null) {
            redisUri.setTimeout(timeout);
        } else {
            redisUri.setTimeout(DEFAULT_TIMEOUT);
        }

        redisClient = RedisClient.create(redisUri);
        connection = redisClient.connect();
        isInit = true;
    }

    //endregion

    /**
     * 获取命令执行
     *
     * @return commands
     */
    private static RedisCommands<String, String> getCommands() {
        if (!isInit) {
            throw new RuntimeException("RedisUtils is not initialized");
        }
        return connection.sync();
    }

    /**
     * 关闭连接
     */
    public static void close() {

        isInit = false;
        try {
            connection.close();
        } catch (Exception ex) {
            //ignore
        }
        try {
            redisClient.shutdown();
        } catch (Exception ex) {
            //ignore
        }
    }


    private RedisUtils() {
        throw new UnsupportedOperationException();
    }
}
