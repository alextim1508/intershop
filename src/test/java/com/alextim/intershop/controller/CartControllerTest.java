package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.exeption.CurrentOrderAbsentException;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ItemMapper itemMapper;

    private Order order;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        order = new Order();
        item = new Item("Test Item", "Test Description","image.jpg", 100.0);
        item.setId(1L);

        itemDto = new ItemDto(1L, "Test Item","Test Description","image.jpg", 2, 100);
    }

    @Test
    void getCart_shouldReturnHtmlWithCards() throws Exception {
        when(orderService.getCurrentOrder()).thenReturn(order);
        when(orderService.getItemsFromOrder(order)).thenReturn(Map.of(item, 2));
        when(itemMapper.toDto(item, 2)).thenReturn(itemDto);
        when(orderService.calcPrice(order)).thenReturn(200.0);

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("items", List.of(itemDto)))
                .andExpect(model().attribute("total", 200.0))
                .andExpect(model().attribute("empty", false));

        verify(orderService, times(1)).getCurrentOrder();
        verify(orderService, times(1)).getItemsFromOrder(order);
        verify(orderService, times(1)).calcPrice(order);
        verify(itemMapper, times(1)).toDto(item, 2);
    }

    @Test
    void changeItemCountInCart_shouldMinusItemToCard() throws Exception {
        doNothing().when(orderService).changeItemCountInCart(1L, Action.MINUS);

        mockMvc.perform(post("/cart/items/{id}", 1)
                        .param("action", "minus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/items"));

        verify(orderService, times(1)).changeItemCountInCart(1L, Action.MINUS);
    }

    @Test
    void getItem_shouldReturnBadRequest() throws Exception {
        when(orderService.getCurrentOrder()).thenThrow(new CurrentOrderAbsentException());

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isInternalServerError());
    }
}