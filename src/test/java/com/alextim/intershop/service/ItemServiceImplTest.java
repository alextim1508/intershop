package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.utils.SortType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceImplTest {

    @MockitoBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void save_shouldSaveItemTest() {
        Item item = new Item();
        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.save(item);

        assertNotNull(savedItem);
        assertEquals(item, savedItem);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void findById_shouldFindByIdTest() {
        long itemId = 1L;
        Item item = new Item();

        Order order = new Order();

        OrderItem orderItem = new OrderItem(order, item);
        orderItem.setCount(5);

        item.setOrderItems(Collections.singletonList(orderItem));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        var result = itemService.findById(itemId);

        assertNotNull(result);
        assertEquals(item, result.getKey());
        assertEquals(5, result.getValue());
    }

    @Test
    void findById_shouldThrowExceptionWhenItemDoesNotExist() {
        long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.findById(itemId));
    }

    @Test
    void search_shouldCallFindAllWithByTitlePageRequestWhenNoSearchTerm() {
        Order order = new Order();
        order.setId(1L);

        Item item1 = new Item();
        item1.setId(1L);

        OrderItem orderItem = new OrderItem(order, item1);
        orderItem.setCount(2);
        item1.setOrderItems(Collections.singletonList(orderItem));

        Item item2 = new Item();
        item2.setId(2L);

        orderItem = new OrderItem(order, item2);
        orderItem.setCount(3);
        item2.setOrderItems(Collections.singletonList(orderItem));

        when(itemRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(item1, item2)));

        Map<Item, Integer> results = itemService.search("", SortType.ALPHA, 0, 10);

        assertEquals(2, results.size());
        verify(itemRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.by("title").ascending()));
        verify(itemRepository, times(0)).search(anyString(), any(PageRequest.class));

        assertEquals(2, results.get(item1));
        assertEquals(3, results.get(item2));
    }

    @Test
    void search_shouldCallSearchAndReturnItemsWhenThereIsSearchTerm() {
        Order order = new Order();
        order.setId(1L);

        Item item1 = new Item();
        item1.setId(1L);

        OrderItem orderItem = new OrderItem(order, item1);
        orderItem.setCount(2);
        item1.setOrderItems(Collections.singletonList(orderItem));

        Item item2 = new Item();
        item2.setId(2L);

        orderItem = new OrderItem(order, item2);
        orderItem.setCount(3);
        item2.setOrderItems(Collections.singletonList(orderItem));

        when(itemRepository.search(anyString(), any(PageRequest.class))).thenReturn(Arrays.asList(item1, item2));

        Map<Item, Integer> results = itemService.search("test", SortType.PRICE, 0, 10);

        assertEquals(2, results.size());
        verify(itemRepository, times(1)).search("test",PageRequest.of(0, 10, Sort.by("price").ascending()));
        verify(itemRepository, times(0)).findAll(any(PageRequest.class));

        assertEquals(2, results.get(item1));
        assertEquals(3, results.get(item2));
    }

    @Test
    void count_shouldCallCountWhenNoSearchTerm() {
        when(itemRepository.count()).thenReturn(5L);

        long count = itemService.count("");

        assertEquals(5L, count);
        verify(itemRepository, times(1)).count();
    }

    @Test
    void count_shouldCallCountBySearchWhenThereIsSearchTerm() {
        String searchTerm = "item";
        when(itemRepository.countBySearch(searchTerm)).thenReturn(3L);

        long count = itemService.count(searchTerm);

        assertEquals(3L, count);
        verify(itemRepository, times(1)).countBySearch(searchTerm);
    }
}