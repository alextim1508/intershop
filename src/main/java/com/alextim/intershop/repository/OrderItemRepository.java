package com.alextim.intershop.repository;

import com.alextim.intershop.entity.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {

    Flux<OrderItem> findByOrderId(long orderId);

    Mono<OrderItem> findByItemIdAndOrderId(long itemId, long orderId);
}
