package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest extends AbstractControllerTestContainer {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    private Item item1, item2, item3;
    private Order order1, order2;

    @BeforeEach
    public void setUp() {
        item1 = itemRepository.save(new Item("item1", "description", "img", 10.0))
                .block();

        item2 = itemRepository.save(new Item("item2", "description", "img", 12.0))
                .block();

        item3 = itemRepository.save(new Item("item3", "description", "img", 20.0))
                .block();


        order1 = orderRepository.save(new Order()).block();
        orderItemRepository.save(new OrderItem(order1.getId(), item1.getId(), 2)).block();
        orderItemRepository.save(new OrderItem(order1.getId(), item2.getId(), 3)).block();

        order1.setStatus(Status.COMPLETED);
        orderRepository.save(order1).block();


        order2 = orderRepository.save(new Order()).block();
        orderItemRepository.save(new OrderItem(order2.getId(), item3.getId(), 1)).block();

        order2.setStatus(Status.COMPLETED);
        orderRepository.save(order2).block();
    }

    @Test
    void getOrders_shouldCompletedOrdersThenReturnOkTest() {
        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html")
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item1"));
                    assertTrue(body.contains("item2"));
                    assertTrue(body.contains("56"));
                    assertTrue(body.contains("item3"));
                    assertTrue(body.contains("20"));
                });
    }

    @Test
    void getOrder_shouldGetCompletedOrderByIdThenReturnOkTest() {
        webTestClient.get()
                .uri("/orders/{id}", order1.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html")
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item1"));
                    assertTrue(body.contains("item2"));
                    assertTrue(body.contains("56"));
                    assertFalse(body.contains("item3"));
                });
    }
}
