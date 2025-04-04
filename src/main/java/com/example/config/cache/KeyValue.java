package com.example.config.cache;

public interface KeyValue {
    /**
     * KEY前缀
     *
     * @return 前缀
     */
    default String getPrefix() {
        return "";
    }
}
