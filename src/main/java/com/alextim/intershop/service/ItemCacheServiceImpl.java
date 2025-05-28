package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static com.alextim.intershop.service.ItemCacheServiceImpl.CacheName.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemCacheServiceImpl implements ItemCacheService {

    private final ReactiveRedisTemplate<String, Item> itemRedisTemplate;
    private final ReactiveRedisTemplate<String, List<Item>> itemListRedisTemplate;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Value("${spring.cache.redis.time-to-live-seconds}")
    public Long ttl;

    enum CacheName {
        ITEM,
        ITEMS_LIST,
        ITEMS_COUNT
    }

    @Override
    public Mono<Boolean> putItem(Item item) {
        String key = String.format("%s:%d", ITEM, item.getId());
        log.info("Put item {} to cache with key \"{}\"", item, key);

        return itemRedisTemplate.opsForValue().set(key, item, Duration.ofSeconds(ttl))
                .onErrorResume(throwable -> {
                    log.error("Put item to cache error", throwable);
                    return Mono.just(false);
                })
                .doOnNext(res -> log.info("Item list {} is {} to cache", item, res ? "put" : "not put"));
    }

    @Override
    public Mono<Item> getItem(long id) {
        String key = String.format("%s:%d", ITEM, id);
        log.info("Get item from cache by key \"{}\"", key);

        return itemRedisTemplate.opsForValue().get(key)
                .onErrorResume(throwable -> {
                    log.error("Get item from cache Cache error", throwable);
                    return Mono.empty();
                })
                .doOnSuccess(item -> {
                    if (item != null) {
                        log.info("Item {} is got from cache", item);
                    } else {
                        log.info("No item was found by key \"{}\"", key);
                    }
                });
    }

    @Override
    public Mono<Boolean> putItemList(String key, List<Item> items) {
        String fullKey = String.format("%s:%s", ITEMS_LIST, key);
        log.info("Put item list {} to cache with key \"{}\"", items, key);

        return itemListRedisTemplate.opsForValue().set(fullKey, items, Duration.ofSeconds(ttl))
                .onErrorResume(throwable -> {
                    log.error("Put item list to cache error", throwable);
                    return Mono.just(false);
                })
                .doOnNext(res -> log.info("Item list {} is {} to cache", items, res ? "put" : "not put"));
    }

    @Override
    public Flux<Item> getItemList(String key) {
        String fullKey = String.format("%s:%s", ITEMS_LIST, key);
        log.info("Get item list from cache by key \"{}\"", key);

        return itemListRedisTemplate.opsForValue().get(fullKey)
                .onErrorResume(throwable -> {
                    log.error("Get item list from cache Cache error", throwable);
                    return Mono.empty();
                })
                .doOnSuccess(items -> {
                    if (items != null) {
                        log.info("Items list {} is got from cache", items);
                    } else {
                        log.info("No item list was found by key \"{}\"", key);
                    }
                })
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Boolean> putItemCount(String key, long value) {
        String fullKey = String.format("%s:%s", ITEMS_COUNT, key);
        log.info("Put item count {} to cache with key {}", value, key);

        return redisTemplate.opsForValue().set(fullKey, String.valueOf(value), Duration.ofSeconds(ttl))
                .onErrorResume(throwable -> {
                    log.error("Put item count to cache error", throwable);
                    return Mono.just(false);
                })
                .doOnNext(res -> log.info("Item count {} is {} to cache", value, res ? "put" : "not put"));
    }

    @Override
    public Mono<Long> getItemCount(String key) {
        String fullKey = String.format("%s:%s", ITEMS_COUNT, key);
        log.info("Get item count from cache by key {}", fullKey);

        return redisTemplate.opsForValue().get(fullKey)
                .onErrorResume(throwable -> {
                    log.error("Get item count from cache error", throwable);
                    return Mono.empty();
                })
                .doOnSuccess(count -> {
                    if (count != null) {
                        log.info("Item count {} is got from cache", count);
                    } else {
                        log.info("No item count was found by key \"{}\"", key);
                    }
                })
                .map(Long::parseLong);
    }
}

