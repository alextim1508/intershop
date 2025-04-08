package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;
    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ItemMapper itemMapper;

    private ItemDto item;

    @BeforeEach
    void setUp() {
        item = new ItemDto(1L, "Test Item", "Test Description", "test.jpg", 5, 100);

        Item testItem = new Item();
        when(itemService.findById(1L)).thenReturn(Map.entry(testItem, 5));
        when(itemMapper.toDto(testItem, 5)).thenReturn(item);
    }

    @Test
    void getItem_shouldReturnHtmlWithItemTest() throws Exception {
        mockMvc.perform(get("/items/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                .andExpect(content().string(containsString("Test Item")))
                .andExpect(content().string(containsString("Test Description")))
                .andExpect(content().string(containsString("100 руб.")))
                .andExpect(content().string(containsString("test.jpg")));

        verify(itemService, times(1)).findById(1L);
        verify(itemMapper, times(1)).toDto(any(Item.class), anyInt());
    }

    @Test
    void changeItemCountInCart_shouldPlusItemToCard() throws Exception {
        mockMvc.perform(post("/items/1")
                        .param("action", "plus"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/1"));

        verify(orderService, times(1)).changeItemCountInCart(1, Action.PLUS);
    }

    @Test
    void getItem_shouldReturnBadRequest() throws Exception {
        when(itemService.findById(1L)).thenThrow(new ItemNotFoundException(1));

        mockMvc.perform(get("/items/{id}", 1))
                .andExpect(status().isBadRequest());
    }
}