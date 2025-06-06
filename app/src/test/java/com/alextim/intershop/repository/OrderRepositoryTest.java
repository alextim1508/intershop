package com.alextim.intershop.repository;

import com.alextim.intershop.AbstractRepoTestContainer;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRepositoryTest extends AbstractRepoTestContainer {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void findByStatus_shouldFindOrderByStatus() {
        Order currentOrder = new Order();
        Order competedOrder = new Order();
        competedOrder.setStatus(Status.COMPLETED);

        Iterable<Order> orders = orderRepository.saveAll(Arrays.asList(currentOrder, competedOrder))
                .thenMany(orderRepository.findByStatus(Status.CURRENT))
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
