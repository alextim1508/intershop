package com.alextim.intershop.controller;

import com.alextim.intershop.WebFluxPostgreSQLTestContainer;
import org.junit.jupiter.api.Test;

public class HomeControllerTest extends WebFluxPostgreSQLTestContainer {

    @Test
    void home_shouldRedirectTest() throws Exception {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/main/items");
    }
}
