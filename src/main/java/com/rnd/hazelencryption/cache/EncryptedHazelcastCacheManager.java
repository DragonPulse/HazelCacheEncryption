package com.rnd.hazelencryption.cache;


import com.rnd.hazelencryption.crypto.CipherService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.Cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EncryptedHazelcastCacheManager extends HazelcastCacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    private CipherService cipherService;

    public EncryptedHazelcastCacheManager(HazelcastInstance hazelcastInstance, CipherService cipherService) {
        super(hazelcastInstance);
        this.cipherService = cipherService;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = caches.get(name);
        if (cache == null) {
            final IMap<Object, Object> map = getHazelcastInstance().getMap(name);
            cache = new EncryptedHazelcastCache(map, cipherService);
            long cacheTimeout = calculateCacheReadTimeout(name);
            ((EncryptedHazelcastCache) cache).setReadTimeout(cacheTimeout);
            final Cache currentCache = caches.putIfAbsent(name, cache);
            if (currentCache != null) {
                cache = currentCache;
            }
        }
        return cache;
    }

    private long calculateCacheReadTimeout(String name) {
        Long timeout = getReadTimeoutMap().get(name);
        return timeout == null ? getDefaultReadTimeout() : timeout;
    }
}
