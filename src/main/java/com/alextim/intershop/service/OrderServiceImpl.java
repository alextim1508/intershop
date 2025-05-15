package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.CurrentOrderAbsentException;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import static com.alextim.intershop.utils.Action.*;
import static com.alextim.intershop.utils.Status.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public Mono<Order> save(Order order) {
        log.info("saving order: {}", order);

        return orderRepository.save(order)
                .doOnNext(it -> log.info("saved order: {}", it));
    }

    @Override
    public Flux<Order> findAllCompletedOrders() {
        log.info("find all completed orders");

        return orderRepository.findByStatus(COMPLETED)
                .doOnNext(order -> log.info("found completed orders: {}", order));
    }

    @Override
    public Mono<Order> findById(long id) {
        log.info("find order by id {}", id);

        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new OrderNotFoundException(id)))
                .doOnNext(order -> log.info("found order: {}", order));
    }

    @Override
    public Mono<Order> findCurrentOrder() {
        log.info("find current order");

        return orderRepository.findByStatus(CURRENT)
                .switchIfEmpty(orderRepository.save(new Order()))
                .next()
                .doOnNext(order -> log.info("found current order: {}", order));
    }

    @Override
    public Flux<? extends Entry<Item, Integer>> findItemsWithQuantityByOrderId(long orderId) {
        log.info("find items with quantity by orderId: {}", orderId);

        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(CurrentOrderAbsentException::new))
                .doOnNext(order -> log.info("found order: {}", order))
                .flatMapMany(order -> orderItemRepository.findByOrderId(order.getId()))
                .doOnNext(orderItem -> log.info("order item: {}", orderItem))
                .collectMap(OrderItem::getItemId, OrderItem::getQuantity)
                .flatMapMany(quantityItemIdsMap ->
                        itemRepository.findAllById(quantityItemIdsMap.keySet()).map(item ->
                                new SimpleImmutableEntry<>(item, quantityItemIdsMap.getOrDefault(item.getId(), 0))
                        ))
                .doOnNext(entry -> log.info("item {} quantity: {}", entry.getKey(), entry.getValue()));
    }

    @Override
    public Mono<Order> completeCurrentOrder() {
        log.info("complete current order");

        return orderRepository.findByStatus(CURRENT)
                .switchIfEmpty(orderRepository.save(new Order()))
                .next()
                .flatMap(currentOrder -> {
                    currentOrder.setStatus(COMPLETED);
                    currentOrder.setCompleted(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));
                    return orderRepository.save(currentOrder);
                })
                .doOnNext(order -> log.info("order {} is completed", order));
    }

    @Override
    public Mono<?> changeItemQuantityInCart(long itemId, Action action) {
        log.info("changeItemQuantityInCart. itemId {}, action: {}", itemId, action);

        return orderRepository.findByStatus(CURRENT)
                .switchIfEmpty(orderRepository.save(new Order()))
                .doOnNext(order -> log.info("cur order: {}", order))
                .next()
                .flatMap(order ->
                        orderItemRepository.findByItemIdAndOrderId(itemId, order.getId())
                                .switchIfEmpty(Mono.just(new OrderItem(order.getId(), itemId, 0)))
                )
                .doOnNext(orderItem -> log.info("orderItem: {}", orderItem))
                .flatMap(orderItem -> {
                    if (action == PLUS) {
                        orderItem.setQuantity(orderItem.getQuantity() + 1);
                        return orderItemRepository.save(orderItem);
                    } else if (action == MINUS) {
                        if (orderItem.getQuantity() > 0)
                            orderItem.setQuantity(orderItem.getQuantity() - 1);
                        else
                            orderItem.setQuantity(0);
                        return orderItemRepository.save(orderItem);
                    } else if (action == DELETE) {
                        return orderItemRepository.deleteById(orderItem.getId());
                    }
                    return Mono.error(new RuntimeException("Unknown action " + action));
                })
                .doOnNext(orderItem -> {
                    if(orderItem.getClass() == OrderItem.class) {
                        log.info("{} is updated", orderItem);
                    } else {
                        log.info("orderItem is deleted"); //todo add condition for deleting
                    }
                });
    }

    @Override
    public double calcPrice(Item item, int quantity) {
        log.info("calc price. item: {}, quantity: {}", item, quantity);
        double price = item.getPrice() * quantity;
        log.info("price: {}", price);
        return price;
    }
}
