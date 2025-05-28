package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.utils.SortType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map.Entry;

public interface ItemService {

    Mono<Item> save(Item item);

    Mono<? extends Entry<Item, Integer>> findItemWithQuantityById(long itemId);

    Flux<? extends Entry<Item, Integer>> findItemsWithQuantity(String search,
                                                               SortType sort,
                                                               int pageNumber,
                                                               int pageSize);

    Mono<Long> count(String search);
}
