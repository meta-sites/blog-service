package com.blog.services;

import javafx.util.Pair;

public interface CacheService {
    String getCache(String name);

    void setCache(Pair<String, String> cache);

    void clearCache(String name);
}
