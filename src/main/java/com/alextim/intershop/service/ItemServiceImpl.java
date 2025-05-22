package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;

import static com.alextim.intershop.utils.Status.CURRENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Mono<Item> save(Item item) {
        log.info("save item: {}", item);

        return itemRepository.save(item)
                .doOnNext(it -> log.info("saved item: {}", it));
    }

    @Override
    public Mono<? extends Entry<Item, Integer>> findItemWithQuantityById(long itemId) {
        log.info("findItemWithQuantityById. itemId: {}", itemId);

        Mono<Item> itemMono = itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(() -> new ItemNotFoundException(itemId)))
                .doOnNext(item -> log.info("found by id {} item: {}", itemId, item));

        Mono<Integer> quantityMono = orderRepository.findByStatus(CURRENT)
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
                orderRepository.findByStatus(CURRENT)
                        .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                        .flatMap(order -> orderItemRepository.findByOrderId(order.getId()))
                        .collectMap(OrderItem::getItemId, OrderItem::getQuantity)
                        .doOnNext(map -> log.info("Quantity-item map in current order: {}", map))
                        .cache();

        Flux<Item> itemFlux;
        if (search.isEmpty()) {
            itemFlux = itemRepository.findAllBy(pageRequest);
        } else {
            itemFlux = itemRepository.findByTitleOrDescriptionContains(search, pageRequest);
        }

        return itemFlux
                .doOnNext(item -> log.info("found item: {}", item))
                .concatMap(item ->
                        quantityItemIdsInCurrentOrderMono.map(quantityItemsMap ->
                                new SimpleImmutableEntry<>(item, quantityItemsMap.getOrDefault(item.getId(), 0))
                        ))
                .doOnNext(entry -> log.info("item {} quantity: {}", entry.getKey(), entry.getValue()));
    }

    @Override
    public Mono<Long> count(String search) {
        log.info("count of items by search string \"{}\"", search);

        if (search.isEmpty()) {
            return itemRepository.count()
                    .doOnNext(count -> log.info("item count: {}", count));
        } else {
            return itemRepository.countByTitleOrDescriptionContains(search)
                    .doOnNext(count -> log.info("item count: {}, search string: {}", count, search));
        }
    }
}
