package com.alextim.intershop.service;


import com.alextim.intershop.entity.Item;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map.Entry;

public interface OrderService {

    Mono<Order> save(Order order);

    Flux<Order> findAllCompletedOrders();

    Mono<Order> findById(long id);

    Mono<Order> findCurrentOrder();

    Flux<? extends Entry<Item, Integer>> findItemsWithQuantityByOrderId(long orderId);

    Mono<Order> completeCurrentOrder();

    Mono<OrderItem> changeItemQuantityInCart(long itemId, Action action);

    double calcPrice(Item item, int quantity);
}
