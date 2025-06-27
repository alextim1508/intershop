package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;

import java.util.List;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

public class BuyControllerTest extends AbstractControllerTestContainer {

    @Test
    void buy_shouldRedirectTest() {
        var authentication = new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
                .mutateWith(csrf())
                .post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/orders/" + order.getId() + "?newOrder=true&rejectedOrder=false");
    }
}
