package com.example.config.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "login.demo")
public class AppCacheProperties {
    /**
     * 缓存
     */
    private CacheProperties cache = new CacheProperties();

    /**
     * 是否刷新路由,默认不刷新
     * <p>
     * 一般用于用户权限发生变化时，刷新路由，如果用户量过多，不建议启用
     * </p>
     */
    public Boolean refreshRoutes = false;

    @Data
    public static class CacheProperties {
        /**
         * 前缀
         */
        private String prefix = "login:demo:cache";
    }

}