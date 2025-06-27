package com.alextim.intershop.repository;

import com.alextim.intershop.AbstractRepoTestContainer;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.utils.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRepositoryTest extends AbstractRepoTestContainer {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void findByUserIdAndStatus_shouldFindOrderByStatus() {
        User user = userRepository.save(new User("user", "pass")).block();

        Order currentOrder = new Order(user.getId());
        Order competedOrder = new Order(user.getId());
        competedOrder.setStatus(Status.COMPLETED);

        Iterable<Order> orders = orderRepository.saveAll(Arrays.asList(currentOrder, competedOrder))
                .thenMany(orderRepository.findByUserIdAndStatus(user.getId(), Status.CURRENT))
                .toIterable();

        Assertions.assertThat(orders)
                .withFailMessage("Orders is empty")
                .isNotEmpty()
                .withFailMessage("Size of orders is not 1")
                .hasSize(1)
                .element(0)
                .withFailMessage("Id is unexpected")
                .extracting(Order::getId)
                .isNotNull();
    }
}
