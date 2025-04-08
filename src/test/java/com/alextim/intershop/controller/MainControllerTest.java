package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.SortType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.alextim.intershop.utils.Action.PLUS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;
    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        Item item = new Item("Test Item", "Test Description", "test.jpg", 100.0);
        item.setId(1L);

        when(itemService.search(anyString(), any(SortType.class), anyInt(), anyInt())).thenReturn(Map.of(item, 5));
        when(itemService.count(anyString())).thenReturn(1L);

        ItemDto itemDto = new ItemDto(1L,"Test Item", "Test Description", "test.jpg", 5, 100);

        when(itemMapper.toDto(any(Item.class), anyInt())).thenReturn(itemDto);

        doNothing().when(orderService).changeItemCountInCart(anyInt(), any(Action.class));
    }

    @Test
    void getItems_shouldReturnHtmlWithAllItems() throws Exception {
        mockMvc.perform(get("/main/items")
                        .param("search", "test")
                        .param("sort", "ALPHA")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(content().string(containsString("Test Item")))
                .andExpect(content().string(containsString("Test Description")))
                .andExpect(content().string(containsString("test.jpg")))
                .andExpect(content().string(containsString("5")))
                .andExpect(content().string(containsString("100 руб.")));

        verify(itemService, times(1)).search("test", SortType.ALPHA, 0, 10);
        verify(itemService, times(1)).count("test");
    }

    @Test
    void changeItemCountInCart_shouldRedirectToItemsHtml() throws Exception {
        mockMvc.perform(post("/main/items/{id}", 1)
                        .param("action", "PLUS")
                        .param("search", "")
                        .param("sort", "NO")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items?search=&sort=NO&pageSize=10&pageNumber=1"));

        verify(orderService, times(1)).changeItemCountInCart(1, PLUS);

    }
}
