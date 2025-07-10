package com.example.config.cache;

public interface KeyValue {
    
    /**
     * 缓存key的失效时间
     * <p> 不设置失效时间 == 永久有效 ；
     * XXX: setObject方法设为-1时，经封装后代表永久有效
     * <p> 单位：秒
     */
    long EXPIRE_TIME_DEFAULT = -1;

    /**
     * KEY前缀
     *
     * @return 前缀
     */
    default String getPrefix() {
        return "";
    }
}
