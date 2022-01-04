package com.cl.common;

import com.cl.common.enums.CacheEnum;
import com.cl.common.utils.CacheUtils;
import com.cl.common.utils.HttpUtils;
import com.cl.common.utils.RedisUtils;

import java.io.IOException;

/**
 * @author: CarterCL
 * @date: 2022/1/1 17:16
 * @version: V1.0
 */
public class Demo {

    public static void main(String[] args) throws Exception {

    }

    private static void httpUtilsDemo() throws IOException {
        String response = HttpUtils.get("https://cn.bing.com");
        System.out.println(response);
    }

    private static void cacheUtilsDemo() {
        CacheUtils.put(CacheEnum.COMMON_CACHE, "key", "value");
        System.out.println(CacheUtils.get(CacheEnum.COMMON_CACHE, "key"));
    }

    private static void redisUtilsDemo() {
        RedisUtils.init("127.0.0.1", 6379, 0, "password");
        RedisUtils.set("key", "value");
        System.out.println(RedisUtils.get("key"));
        RedisUtils.close();
    }
}
