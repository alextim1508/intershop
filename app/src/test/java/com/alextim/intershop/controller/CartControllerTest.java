package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

public class CartControllerTest extends AbstractControllerTestContainer {

    @Test
    void getCart_shouldGetCurrentOrderThenReturnOkTest() {
        var authentication = new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        webTestClient.mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
                .get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html")
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item1"));
                    assertTrue(body.contains("item2"));
                    assertFalse(body.contains("item3"));
                });
    }

    @Test
    void changeItemQuantityInCart_shouldChangeItemQuantityThenRedirectTest() {
        var authentication = new UsernamePasswordAuthenticationToken(user, "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("action", "PLUS", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=action")
                .header("Content-type", "text/plain");

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockAuthentication(authentication))
                .mutateWith(csrf())
                .post()
                .uri("/cart/items/{id}", item1.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/cart/items");
    }
}
