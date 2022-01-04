package com.cl.common.utils;

import com.cl.common.enums.CacheEnum;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 本地缓存工具类
 *
 * @author: CarterCL
 * @date: 2022/1/1 14:40
 * @version: V1.0
 */
public final class CacheUtils {

    private static final Map<CacheEnum, Cache<String, Object>> CACHE_MAP = new ConcurrentHashMap<>(CacheEnum.values().length * 2);

    static {
        // 初始化
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            CACHE_MAP.put(cacheEnum, Caffeine.from(cacheEnum.toCaffeineSpec()).build());
        }
    }

    /**
     * 获取值
     *
     * @param cacheEnum 缓存类型
     * @param key       key
     * @return value
     */
    public static Object get(CacheEnum cacheEnum, String key) {
        return CACHE_MAP.get(cacheEnum).getIfPresent(key);
    }

    /**
     * 获取值，若获取不到，则执行自定义方法设置值
     *
     * @param cacheEnum     缓存类型
     * @param key           key
     * @param valueFunction 自定义方法
     * @return value
     */
    public static Object getAndSet(CacheEnum cacheEnum, String key, Function<String, Object> valueFunction) {
        return CACHE_MAP.get(cacheEnum).get(key, valueFunction);
    }

    /**
     * 设置值
     *
     * @param cacheEnum 缓存类型
     * @param key       key
     * @param value     value
     */
    public static void put(CacheEnum cacheEnum, String key, Object value) {
        CACHE_MAP.get(cacheEnum).put(key, value);
    }

    /**
     * 设置值，若不存在则返回null,存在返回已存在的value
     *
     * @param cacheEnum 缓存类型
     * @param key       key
     * @param value     value
     * @return value
     */
    public static Object putIfAbsent(CacheEnum cacheEnum, String key, Object value) {
        return CACHE_MAP.get(cacheEnum).asMap().putIfAbsent(key, value);
    }


    /**
     * 删除key
     *
     * @param cacheEnum 缓存类型
     * @param key       key
     */
    public static void del(CacheEnum cacheEnum, String key) {
        CACHE_MAP.get(cacheEnum).invalidate(key);
    }
}
