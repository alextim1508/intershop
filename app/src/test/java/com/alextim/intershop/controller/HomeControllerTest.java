package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import org.junit.jupiter.api.Test;

public class HomeControllerTest extends AbstractControllerTestContainer {

    @Test
    void home_shouldRedirectTest() throws Exception {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/main/items");
    }
}
