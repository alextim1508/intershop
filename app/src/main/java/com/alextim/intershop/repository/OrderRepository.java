package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Status;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByUserIdAndStatus(long userId, Status status);
}
