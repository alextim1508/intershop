package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.SortType;
import com.alextim.intershop.utils.Status;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemCacheService itemCacheService;

    @Override
    public Mono<Item> save(Item item) {
        log.info("save item: {}", item);

        return itemRepository.save(item)
                .doOnNext(savedItem -> log.info("saved item: {}", savedItem))
                .flatMap(savedItem ->
                        itemCacheService.putItem(savedItem)
                                .then(Mono.just(savedItem))
                );
    }

    @Override
    public Mono<? extends Entry<Item, Integer>> findItemWithQuantityById(long itemId) {
        log.info("findItemWithQuantityById. itemId: {}", itemId);

        Mono<Item> itemFromRepoMono = Mono.defer(() ->
                itemRepository.findById(itemId)
                    .switchIfEmpty(Mono.error(() -> new ItemNotFoundException(itemId)))
                    .doOnNext(item -> log.info("found by id {} item: {}", itemId, item))
        );

        Mono<Item> itemMono = itemCacheService.getItem(itemId)
                .switchIfEmpty(itemFromRepoMono)
                .flatMap(item ->
                        itemCacheService.putItem(item)
                                .then(Mono.just(item))
                );

        Mono<Integer> quantityMono = orderRepository.findByStatus(Status.CURRENT)
                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                .next()
                .flatMap(order -> orderItemRepository.findByItemIdAndOrderId(itemId, order.getId()))
                .doOnNext(orderItem -> log.info("found order-item: {}", orderItem))
                .map(OrderItem::getQuantity)
                .defaultIfEmpty(0);

        return Mono.zip(itemMono, quantityMono)
                .map(tuple -> new SimpleImmutableEntry<>(tuple.getT1(), tuple.getT2()))
                .doOnNext(entry -> log.info("item: {}, quantity: {}", entry.getKey(), entry.getValue()));
    }

    @Override
    public Flux<? extends Entry<Item, Integer>> findItemsWithQuantity(String search,
                                                                      SortType sort,
                                                                      int pageNumber,
                                                                      int pageSize) {
        log.info("findItemsWithQuantity. search: \"{}\", sort: {}, pageNumber: {}, pageSize: {}",
                search, sort, pageNumber, pageSize);

        PageRequest pageRequest = switch (sort) {
            case NO -> PageRequest.of(pageNumber, pageSize);
            case ALPHA -> PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            case PRICE -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
        };
        log.info("pageRequest: {}", pageRequest);

        Mono<Map<Long, Integer>> quantityItemIdsInCurrentOrderMono =
                orderRepository.findByStatus(Status.CURRENT)
                        .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                        .flatMap(order -> orderItemRepository.findByOrderId(order.getId()))
                        .collectMap(OrderItem::getItemId, OrderItem::getQuantity)
                        .doOnNext(map -> log.info("Quantity-item map in current order: {}", map))
                        .cache();

        Flux<Item> itemFromRepoFlux = Flux.defer(() -> {
            if (search.isEmpty()) {
                return itemRepository.findAllBy(pageRequest);
            } else {
                return itemRepository.findByTitleOrDescriptionContains(search, pageRequest);
            }
        });

        String fullKey = generateCacheKey(search, sort, pageNumber, pageSize);

        Flux<Item> itemFlux = itemCacheService.getItemList(fullKey)
                .switchIfEmpty(itemFromRepoFlux)
                .collectList()
                .flatMap(items ->
                        itemCacheService.putItemList(fullKey, items)
                                .then(Mono.just(items))
                )
                .flatMapMany(Flux::fromIterable);

        return itemFlux
                .doOnNext(item -> log.info("found item: {}", item))
                .concatMap(item ->
                        quantityItemIdsInCurrentOrderMono.map(quantityItemsMap ->
                                new SimpleImmutableEntry<>(item, quantityItemsMap.getOrDefault(item.getId(), 0))
                        ))
                .doOnNext(entry -> log.info("item {} quantity: {}", entry.getKey(), entry.getValue()));
    }

    static String generateCacheKey(String search,
                            SortType sort,
                            int pageNumber,
                            int pageSize) {
        return new StringJoiner("-")
                .add(search.isEmpty() ? "EMPTY" : search)
                .add(sort.name())
                .add(Integer.toString(pageNumber))
                .add(Integer.toString(pageSize))
                .toString();

    }

    @Override
    public Mono<Long> count(String search) {
        log.info("count of items by search string \"{}\"", search);

        Mono<Long> countFromRepoMono = Mono.defer(() -> {
            if (search.isEmpty()) {
                return itemRepository.count()
                        .doOnNext(count -> log.info("item count: {}", count));
            } else {
                return itemRepository.countByTitleOrDescriptionContains(search)
                        .doOnNext(count -> log.info("item count: {}, search string: {}", count, search));
            }
        });

        String fullKey = generateCacheKey(search);

        return itemCacheService.getItemCount(fullKey)
                .switchIfEmpty(countFromRepoMono)
                .flatMap(count ->
                        itemCacheService.putItemCount(fullKey, count)
                                .then(Mono.just(count))
                );
    }

    static String generateCacheKey(String str) {
        return str.isEmpty() ? "EMPTY" : str;
    }
}
