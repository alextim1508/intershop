package com.alextim.intershop.controller;

import com.alextim.intershop.WebFluxPostgreSQLTestContainer;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GlobalExceptionHandlerTest extends WebFluxPostgreSQLTestContainer {

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRepository.save(new Item("item1", "description", "img", 12.1))
                .block();
    }

    @Test
    void getItem_shouldReturnBadRequestWhenItemDoesNotExist() {
        webTestClient.get()
                .uri("/items/99")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
