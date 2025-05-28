package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;


public class ItemControllerTest extends AbstractControllerTestContainer {

    @Autowired
    ItemRepository itemRepository;

    private Item item1, item2;

    @BeforeEach
    public void setUp() {
        item1 = itemRepository.save(new Item("item1", "description", "img", 12.1))
                .block();
        item2 = itemRepository.save(new Item("item2", "description", "img", 12.1))
                .block();
    }

    @Test
    void getItem_shouldGetItemByIdThenReturnOkTest() {
        webTestClient.get()
                .uri("/items/" + item1.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                    assertTrue(body.contains("item1"));
                    assertFalse(body.contains("item2"));
                });
    }

    @Test
    void changeItemQuantityInCart_shouldChangeItemQuantityThenRedirectTest() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("action", "PLUS", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=action")
                .header("Content-type", "text/plain");

        webTestClient.post()
                .uri("/items/{id}", item1.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items/" + item1.getId());
    }
}
