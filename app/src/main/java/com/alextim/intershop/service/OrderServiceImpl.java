package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.Status;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Mono<Order> save(Order order) {
        log.info("save order: {}", order);

        return orderRepository.save(order)
                .doOnNext(savedOrder -> log.info("saved order: {}", savedOrder));
    }

    @Override
    public Flux<Order> findAllCompletedOrders() {
        log.info("find all completed orders");

        return orderRepository.findByStatus(Status.COMPLETED)
                .doOnNext(order -> log.info("found completed order: {}", order));
    }

    @Override
    public Mono<Order> findById(long id) {
        log.info("find order by id {}", id);

        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new OrderNotFoundException(id)))
                .doOnNext(order -> log.info("found by id {} order: {}", id, order));
    }

    @Override
    public Mono<Order> findCurrentOrder() {
        log.info("find current order");

        return orderRepository.findByStatus(Status.CURRENT)
                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                .next()
                .doOnNext(order -> log.info("found current order: {}", order));
    }

    @Override
    public Flux<? extends Entry<Item, Integer>> findItemsWithQuantityByOrderId(long orderId) {
        log.info("find items with quantity by orderId: {}", orderId);

        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(() -> new OrderNotFoundException(orderId)))
                .doOnNext(order -> log.info("found by id {} order: {}", orderId, order))
                .flatMapMany(order ->
                        orderItemRepository.findByOrderId(order.getId())
                                .filter(orderItem -> orderItem.getQuantity() > 0)
                )
                .doOnNext(orderItem -> log.info("found order-item: {}", orderItem))
                .collectMap(OrderItem::getItemId, OrderItem::getQuantity)
                .doOnNext(map -> log.info("quantity itemIds map: {}", map))
                .flatMapMany(quantityItemIdsMap ->
                        itemRepository.findAllById(quantityItemIdsMap.keySet()).map(item ->
                                new SimpleImmutableEntry<>(item, quantityItemIdsMap.getOrDefault(item.getId(), 0))
                        ))
                .doOnNext(entry -> log.info("item {} quantity: {}", entry.getKey(), entry.getValue()));
    }

    @Override
    public Mono<Order> completeCurrentOrder() {
        log.info("complete current order");

        return orderRepository.findByStatus(Status.CURRENT)
                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                .next()
                .flatMap(currentOrder -> {
                    currentOrder.setStatus(Status.COMPLETED);
                    currentOrder.setCompleted(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));
                    return orderRepository.save(currentOrder);
                })
                .doOnNext(order -> log.info("order {} is completed", order));
    }

    @Override
    public Mono<OrderItem> changeItemQuantityInCart(long itemId, Action action) {
        log.info("changeItemQuantityInCart. itemId {}, action: {}", itemId, action);

        return orderRepository.findByStatus(Status.CURRENT)
                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order())))
                .doOnNext(order -> log.info("cur order: {}", order))
                .next()
                .flatMap(order ->
                        orderItemRepository.findByItemIdAndOrderId(itemId, order.getId())
                                .switchIfEmpty(Mono.defer(() -> Mono.just(new OrderItem(order.getId(), itemId, 0))))
                )
                .doOnNext(orderItem -> log.info("order-item: {}", orderItem))
                .flatMap(orderItem -> {
                    if (action == Action.PLUS) {
                        orderItem.setQuantity(orderItem.getQuantity() + 1);
                        return orderItemRepository.save(orderItem);
                    } else if (action == Action.MINUS) {
                        if (orderItem.getQuantity() > 0)
                            orderItem.setQuantity(orderItem.getQuantity() - 1);
                        else
                            orderItem.setQuantity(0);
                        return orderItemRepository.save(orderItem);
                    } else if (action == Action.DELETE) {
                        return orderItemRepository.deleteById(orderItem.getId())
                                .doOnSuccess(v -> log.info("order item {} is deleted", orderItem.getId()))
                                .then(Mono.empty());
                    }
                    return Mono.error(new RuntimeException("Unknown action " + action));
                })
                .doOnNext(orderItem -> log.info("{} is updated", orderItem));
    }

    @Override
    public double calcPrice(Item item, int quantity) {
        log.info("calc price. item: {}, quantity: {}", item, quantity);
        double price = item.getPrice() * quantity;
        log.info("price: {}", price);
        return price;
    }
}
