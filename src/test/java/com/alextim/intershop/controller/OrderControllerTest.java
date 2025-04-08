package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.dto.OrderDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        OrderDto orderDto = new OrderDto(
                1L,
                List.of(new ItemDto(1L,"Test Item", "Test Description","image.jpg", 2, 1000)),
                2000.0);

        Order order = new Order();
        order.setId(1L);

        ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description","image.jpg", 2, 1000);

        Item item = new Item("Test Item", "Test Description","image.jpg", 1000.0);
        item.setId(1L);

        OrderItem orderItem = new OrderItem(order, item);
        orderItem.setId(1L);
        orderItem.setCount(2);

        order.setOrderItems(List.of(orderItem));

        when(orderService.findAllCompletedOrders()).thenReturn(List.of(order));
        when(orderService.findById(anyLong())).thenReturn(order);

        when(orderService.getItemsFromOrder(any(Order.class))).thenReturn(Map.of(item, 2));

        when(itemMapper.toDto(any(Item.class), anyInt())).thenReturn(itemDto);

        when(orderService.calcPrice(any(Order.class))).thenReturn(2000.0);
    }

    @Test
    void getOrders_shouldReturnHTMLWithOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(content().string(containsString("Заказ №1")))
                .andExpect(content().string(containsString("Test Item")))
                .andExpect(content().string(containsString("Сумма: 2000.0 руб.")));
    }

    @Test
    void getOrder_shouldReturnHTMLWithOrder() throws Exception {
        mockMvc.perform(get("/orders/1").param("newOrder", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("newOrder"))
                .andExpect(model().attribute("newOrder", true))
                .andExpect(content().string(containsString("Test Item")));

    }

    @Test
    void getItem_shouldReturnBadRequest() throws Exception {
        when(orderService.findById(1L)).thenThrow(new OrderNotFoundException(1));

        mockMvc.perform(get("/orders/{id}", 1))
                .andExpect(status().isBadRequest());
    }
}
