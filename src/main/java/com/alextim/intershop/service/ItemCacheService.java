package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemCacheService {

    Mono<Boolean> putItem(Item item);

    Mono<Item> getItem(long id);

    Mono<Boolean> putItemList(String key, List<Item> items);

    Flux<Item> getItemList(String key);

    Mono<Boolean> putItemCount(String key, long value);

    Mono<Long> getItemCount(String key);
}
