package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest extends AbstractControllerTestContainer {

    @BeforeEach
    public void setUp() {
        super.setUp();

        order.setStatus(Status.COMPLETED);
        orderRepository.save(order).block();

        Order order2 = orderRepository.save(new Order(user.getId())).block();
        orderItemRepository.save(new OrderItem(order2.getId(), item3.getId(), 1)).block();

        order2.setStatus(Status.COMPLETED);
        orderRepository.save(order2).block();
    }

    @Test
    void getOrders_shouldCompletedOrdersThenReturnOkTest() {
        var authentication = new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
                .get()
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
                    assertTrue(body.contains("63")); /* Price 12*2 + 13*3 */
                });
    }

    @Test
    void getOrder_shouldGetCompletedOrderByIdThenReturnOkTest() {
        var authentication = new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
                .get()
                .uri("/orders/{id}", order.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html")
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item1"));
                    assertTrue(body.contains("item2"));
                    assertTrue(body.contains("63"));
                    assertFalse(body.contains("item3"));
                });
    }
}
