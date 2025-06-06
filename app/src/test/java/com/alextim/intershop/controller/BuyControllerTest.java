package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BuyControllerTest extends AbstractControllerTestContainer {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void buy_shouldRedirectTest() {
        Order order = orderRepository.save(new Order()).block();

        webTestClient.post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/orders/" + order.getId() + "?newOrder=true&rejectedOrder=false");
    }
}
