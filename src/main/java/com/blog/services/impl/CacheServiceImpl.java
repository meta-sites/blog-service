package com.blog.services.impl;

import com.blog.services.CacheService;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CacheServiceImpl implements CacheService {
    private final int CACHE_SIZE = 100;
    private final int MAX_CACHE_SIZE = 10000;

    private Map<String, String> cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    @Override
    public String getCache(String key) {
        return cache.get(key);
    }

    @Override
    public void setCache(Pair< String, String > cachePair) {
        cache.put(cachePair.getKey(), cachePair.getValue());
    }

    @Override
    public void clearCache(String name) {
        cache.remove(name);
    };
}
