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


public class MainControllerTest extends AbstractControllerTestContainer {

    @Autowired
    ItemRepository itemRepository;

    private Item item1, item2, item3;

    @BeforeEach
    public void setUp() {
        item1 = itemRepository.save(new Item("item1", "description", "img", 12.1))
                .block();

        item2 = itemRepository.save(new Item("item2", "description", "img", 12.1))
                .block();

        item3 = itemRepository.save(new Item("thing", "description", "img", 12.1))
                .block();
    }

    @Test
    void getItem_shouldGetItemByIdThenReturnOkTest() {
        webTestClient.get()
                .uri("/main/items")
                .attribute("search", "item")
                .attribute("sort", "NO")
                .attribute("pageSize", 10)
                .attribute("pageNumber", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
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
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("action", "PLUS", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=action")
                .header("Content-type", "text/plain");

        bodyBuilder.part("search", "", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=search")
                .header("Content-type", "text/plain");

        bodyBuilder.part("sort", "ALPHA", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=sort")
                .header("Content-type", "text/plain");

        bodyBuilder.part("pageNumber", "0", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=pageNumber")
                .header("Content-type", "text/plain");

        bodyBuilder.part("pageSize", "5", MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "form-data; name=pageSize")
                .header("Content-type", "text/plain");

        webTestClient.post()
                .uri("/main/items/{id}", item1.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/main/items?search=&sort=ALPHA&pageSize=5&pageNumber=0");
    }
}
