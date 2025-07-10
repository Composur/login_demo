package com.example.config.cache;

import java.util.Optional;

public interface ConfigCacheProvider {
    boolean cacheConfig(String key, String value);

    Optional<String> getConfig(String key);

    boolean clearConfig(String key);
}
