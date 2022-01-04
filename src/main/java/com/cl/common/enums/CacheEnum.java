package com.cl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * 缓存枚举
 *
 * @author: CarterCL
 * @date: 2022/1/1 14:33
 * @version: V1.0
 */
@Getter
@AllArgsConstructor
public enum CacheEnum {

    /**
     * 通用缓存
     */
    COMMON_CACHE("common", false, null, 10000),

    /**
     * 验证码缓存
     */
    VERIFY_CODE_CACHE("verifyCode", false, Duration.ofMinutes(3), 50000);

    /**
     * 缓存名
     */
    private final String cacheName;

    /**
     * 缓存时间是否根据访问刷新
     */
    private final boolean isAfterAccess;

    /**
     * TTL
     */
    private final Duration duration;

    /**
     * 最大缓存个数
     */
    private final long maximumSize;

    /**
     * 转换为CaffeineSpec配置
     *
     * @return 配置字符串
     */
    public String toCaffeineSpec() {
        StringBuilder builder = new StringBuilder();
        builder.append("maximumSize=").append(maximumSize);
        if (duration != null) {
            if (isAfterAccess) {
                builder.append(",expireAfterAccess=").append(duration.toSeconds()).append("s");
            } else {
                builder.append(",expireAfterWrite=").append(duration.toSeconds()).append("s");
            }
        }
        return builder.toString();
    }
}
