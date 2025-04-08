package com.alextim.intershop.controller;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuyController.class)
class BuyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void buy_shouldCompleteOrderAndRedirectToOrdersHtml() throws Exception {
        Order completedOrder = new Order();
        completedOrder.setId(1L);

        when(orderService.completeCurrentOrder()).thenReturn(completedOrder);

        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/1?newOrder=true"));

        verify(orderService, times(1)).completeCurrentOrder();
    }
}