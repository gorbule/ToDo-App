package com.todo.app.demo.ehcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * EhCache configuration class.
 * @Configuration - Indicates that a class declares one or more @Bean methods and may be processed
 * by the Spring container to generate bean definitions and service requests for those.
 * @EnableCaching annotation enables Spring's annotation-driven cache management.
 * Spring's auto-configuration finds Ehcache's implementation of JSR-107.
 * No caches are created by default.
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
