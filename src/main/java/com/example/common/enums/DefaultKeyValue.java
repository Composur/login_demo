package com.example.common.enums;

import com.example.config.cache.KeyValue;
import lombok.Getter;

@Getter
public enum DefaultKeyValue implements KeyValue {
    SYS_CONFIG("sys", EXPIRE_TIME_DEFAULT, CacheType.STRING, String.class, "系统配置", "sys:key --> (String)value");

    private final String prefix;

    private final Long expire;

    private CacheType type;

    private final Class<?> clazz;

    private final String name;

    private final String desc;

    DefaultKeyValue(String prefix, long expire, CacheType type, Class<?> clazz, String name, String desc) {
        this.prefix = prefix;
        this.expire = expire;
        this.clazz = clazz;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    /**
     * JimDB缓存的数据类型
     * <p> 涉及如何 set、get </p>
     */
    public enum CacheType {
        STRING, OBJECT, HASHMAP, SET, LIST
    }
}
