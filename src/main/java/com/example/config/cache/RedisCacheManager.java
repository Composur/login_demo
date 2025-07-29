package com.example.config.cache;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisCacheManager {
    private final RedisCacheProperties redisCacheProperties;
    private final StringRedisTemplate redisTemplate;


    /**
     * 正规化缓存KEY,按照<code>:</code>进行拼接
     *
     * @param key key
     * @return <code>"prefix:key"</code>
     */
    public String normaliz(String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        return redisCacheProperties.getCache().getPrefix() + ":" + key;
    }

    /**
     * 删除缓存
     *
     * @param key,自动追加前缀{@link #normaliz(String)}
     * @return .
     */
    public boolean del(final String key) {
        String _key = normaliz(key);
        return Boolean.TRUE.equals(this.redisTemplate.delete(_key));
    }


    /**
     * 设置永久缓存
     *
     * @param key   key,自动追加前缀{@link #normaliz(String)}
     * @param value .
     * @return .
     */
    public boolean set(final String key, String value) {
        return set(key, value, -1);
    }

    /**
     * 设置缓存,单位时间/秒
     *
     * @param key     key,自动追加前缀{@link #normaliz(String)}
     * @param value   .
     * @param timeout timeout,如果为-l,则永久缓存
     * @return .
     */
    public boolean set(final String key, String value, long timeout) {
        return set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存
     *
     * @param key     key,自动追加前缀{@link #normaliz(String)}
     * @param value   .
     * @param timeout timeout,如果为-l,则永久缓存
     * @param unit    time unit
     * @return .
     */
    public boolean set(final String key, String value, long timeout, TimeUnit unit) {
        if (StrUtil.isBlank(key)) {
            String prefix = redisCacheProperties.getCache().getPrefix();
            log.warn("【{}】 set cache value warn, key missing", prefix);
            return false;
        }
        String _key = normaliz(key);
        String prefix = redisCacheProperties.getCache().getPrefix();
        log.debug("【{}】set cache value : key: {}, value: {}, timeout: {}", prefix, _key, value, timeout);
        try {
            if (timeout == -1) {
                this.redisTemplate.opsForValue().set(_key, value);
            } else {
                this.redisTemplate.opsForValue().set(_key, value, timeout, unit);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Redis操作失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取缓存
     *
     * @param key,自动追加前缀{@link #normaliz(String)}
     * @return .
     */
    public Optional<String> getStr(final String key) {
        String _key = normaliz(key);
        return Optional.ofNullable(this.redisTemplate.opsForValue().get(_key));
    }

    /**
     * 检查是否有 key
     */
    public boolean hasKey(String key) {
        return this.redisTemplate.hasKey(normaliz(key));
    }
}
