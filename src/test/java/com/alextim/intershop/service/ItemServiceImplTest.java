package com.alextim.intershop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Test
    void save_shouldAddCountItemInCartTest() {
    }

}