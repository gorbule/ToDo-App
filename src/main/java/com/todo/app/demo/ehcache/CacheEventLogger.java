package com.todo.app.demo.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        LOG.info("Cache event CREATED for ToD Tasks List. CacheEvent Type = {}, Old value = {}, New value = {}",
                cacheEvent.getType(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
