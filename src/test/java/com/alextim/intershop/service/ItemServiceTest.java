package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.SortType;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static com.alextim.intershop.service.ItemServiceImpl.generateCacheKey;
import static com.alextim.intershop.utils.Status.CURRENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ItemServiceImpl.class})
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockitoBean
    private ItemCacheService itemCacheService;
    @MockitoBean
    private ItemRepository itemRepository;
    @MockitoBean
    private OrderRepository orderRepository;
    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @Test
    void save_shouldCreateItemTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(item));
        when(itemCacheService.putItem(any(Item.class))).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.save(item))
                .expectNext(item)
                .verifyComplete();

        verify(itemRepository, times(1)).save(item);
        verify(itemCacheService, times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldFindItemWithQuantityWhenItemDoesNotExistInCacheByIdTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        Order order = new Order();
        order.setId(2L);

        OrderItem orderItem = new OrderItem(item.getId(), order.getId(), 5);

        when(itemRepository.findById(anyLong())).thenReturn(Mono.just(item));
        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(order));
        when(orderItemRepository.findByItemIdAndOrderId(anyLong(), anyLong()))
                .thenReturn(Mono.just(orderItem));
        when(itemCacheService.getItem(any(Long.class))).thenReturn(Mono.empty());
        when(itemCacheService.putItem(any(Item.class))).thenReturn(Mono.just(true));


        StepVerifier.create(itemService.findItemWithQuantityById(item.getId()))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 5)
                .verifyComplete();

        verify(itemRepository, times(1)).findById(item.getId());
        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderItemRepository, times(1)).findByItemIdAndOrderId(item.getId(), order.getId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(itemCacheService, times(1)).getItem(item.getId());
        verify(itemCacheService, times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldGetItemFromCacheAndAddQuantityTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        Order order = new Order();
        order.setId(2L);

        OrderItem orderItem = new OrderItem(item.getId(), order.getId(), 5);

        when(itemRepository.findById(anyLong())).thenReturn(Mono.just(item));
        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(order));
        when(orderItemRepository.findByItemIdAndOrderId(anyLong(), anyLong()))
                .thenReturn(Mono.just(orderItem));
        when(itemCacheService.getItem(any(Long.class))).thenReturn(Mono.just(item));
        when(itemCacheService.putItem(any(Item.class))).thenReturn(Mono.just(true));


        StepVerifier.create(itemService.findItemWithQuantityById(item.getId()))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 5)
                .verifyComplete();

        verify(itemRepository, never()).findById(anyLong());
        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderItemRepository, times(1)).findByItemIdAndOrderId(item.getId(), order.getId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(itemCacheService, times(1)).getItem(item.getId());
        verify(itemCacheService, times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldDoesNotFindItemByIdTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        when(itemRepository.findById(anyLong())).thenReturn(Mono.empty());
        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(new Order()));
        when(itemCacheService.getItem(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(itemService.findItemWithQuantityById(1L))
                .expectError(ItemNotFoundException.class)
                .verify();

        verify(itemRepository, times(1)).findById(item.getId());
        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void findItemsWithQuantity_shouldFindItemsWithQuantityByEmptySearchStringAndNoSortWhenItemDoesNotExistInCacheTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Item1", "Description 1", "img", 100.0);
        item1.setId(1L);
        Item item2 = new Item("Item2", "Description 2", "img", 200.0);
        item2.setId(2L);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));
        when(itemRepository.findAllBy(any(PageRequest.class))).thenReturn(Flux.just(item1, item2));
        when(orderItemRepository.findByOrderId(anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), 1L, 10),
                        new OrderItem(currentOrder.getId(), 2L, 5)
                ));
        when(itemCacheService.getItemList(anyString())).thenReturn(Flux.empty());
        when(itemCacheService.putItemList(anyString(), anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 10)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 5)
                .verifyComplete();


        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, never()).save(any(Order.class));
        verify(itemRepository, times(1)).findAllBy(PageRequest.of(0, 10));
        verify(orderItemRepository, times(1)).findByOrderId(currentOrder.getId());
        String key = generateCacheKey("", SortType.NO, 0, 10);
        verify(itemCacheService, times(1)).getItemList(key);
        verify(itemCacheService, times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldGetItemsFromCacheAndAddQuantityTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Item1", "Description 1", "img", 100.0);
        item1.setId(1L);
        Item item2 = new Item("Item2", "Description 2", "img", 200.0);
        item2.setId(2L);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));
        when(itemRepository.findAllBy(any(PageRequest.class))).thenReturn(Flux.just(item1, item2));
        when(orderItemRepository.findByOrderId(anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), 1L, 10),
                        new OrderItem(currentOrder.getId(), 2L, 5)
                ));
        when(itemCacheService.getItemList(anyString())).thenReturn(Flux.just(item1, item2));
        when(itemCacheService.putItemList(anyString(), anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 10)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 5)
                .verifyComplete();


        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, never()).save(any(Order.class));
        verify(itemRepository, never()).findAllBy(any(PageRequest.class));
        verify(orderItemRepository, times(1)).findByOrderId(currentOrder.getId());
        String key = generateCacheKey("", SortType.NO, 0, 10);
        verify(itemCacheService,times(1)).getItemList(key);
        verify(itemCacheService, times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldFindItemsWithQuantityBySearchStringAndSortWhenItemDoesNotExistInCacheTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Apple", "Fresh apple", "img", 50.0);
        item1.setId(1L);
        Item item2 = new Item("Banana", "Yellow banana", "img", 30.0);
        item2.setId(2L);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));

        when(orderItemRepository.findByOrderId(anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), item1.getId(), 7)
                ));
        when(itemRepository.findByTitleOrDescriptionContains(anyString(), any(PageRequest.class)))
                .thenReturn(Flux.just(item1, item2));
        when(itemCacheService.getItemList(anyString())).thenReturn(Flux.empty());
        when(itemCacheService.putItemList(anyString(), anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("test", SortType.ALPHA, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 7)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 0)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, never()).save(any(Order.class));
        verify(itemRepository, times(1)).findByTitleOrDescriptionContains("test",
                PageRequest.of(0, 10, Sort.by("title").ascending()));

        String key = generateCacheKey("test", SortType.ALPHA, 0, 10);
        verify(itemCacheService, times(1)).getItemList(key);
        verify(itemCacheService, times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldCreateNewOrderThenFindItemsWithQuantityTest() {
        Order newOrder = new Order();
        newOrder.setId(1L);

        Item item = new Item("Item", "Description", "img", 100.0);
        item.setId(1L);

        when(orderRepository.findByStatus(CURRENT)).thenReturn(Flux.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(newOrder));
        when(itemRepository.findAllBy(any())).thenReturn(Flux.just(item));
        when(orderItemRepository.findByOrderId(anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(newOrder.getId(), item.getId(), 3)
                ));
        when(itemCacheService.getItemList(anyString())).thenReturn(Flux.empty());
        when(itemCacheService.putItemList(anyString(), anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 3)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(itemRepository, times(1)).findAllBy(PageRequest.of(0, 10));
        verify(orderItemRepository, times(1)).findByOrderId(newOrder.getId());

        String key = generateCacheKey("", SortType.NO, 0, 10);
        verify(itemCacheService, times(1)).getItemList(key);
        verify(itemCacheService, times(1)).putItemList(key, Arrays.asList(item));
    }

    @Test
    public void count_shouldGetCountItemsBySearchEmptyStringTest() {
        String search = "";
        long totalCount = 10L;

        when(itemRepository.count()).thenReturn(Mono.just(totalCount));
        when(itemCacheService.getItemCount(anyString())).thenReturn(Mono.empty());
        when(itemCacheService.putItemCount(anyString(), anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        verify(itemRepository).count();
        verify(itemRepository, never()).countByTitleOrDescriptionContains(anyString());

        String key = generateCacheKey(search);
        verify(itemCacheService, times(1)).getItemCount(key);
        verify(itemCacheService, times(1)).putItemCount(key, totalCount);
    }

    @Test
    public void count_shouldGetCountItemsBySearchStringTest() {
        String search = "test";
        long totalCount = 10L;

        when(itemRepository.countByTitleOrDescriptionContains(anyString())).thenReturn(Mono.just(totalCount));
        when(itemCacheService.getItemCount(anyString())).thenReturn(Mono.empty());
        when(itemCacheService.putItemCount(anyString(), anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        verify(itemRepository).countByTitleOrDescriptionContains(search);
        verify(itemRepository, never()).count();

        String key = generateCacheKey(search);
        verify(itemCacheService, times(1)).getItemCount(key);
        verify(itemCacheService, times(1)).putItemCount(key, totalCount);
    }

    @Test
    public void count_shouldGetCountItemsBySearchStringFromCacheTest() {
        String search = "test";
        long totalCount = 10L;

        when(itemRepository.countByTitleOrDescriptionContains(anyString())).thenReturn(Mono.just(totalCount));
        when(itemCacheService.getItemCount(anyString())).thenReturn(Mono.just(totalCount));
        when(itemCacheService.putItemCount(anyString(), anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        verify(itemRepository, never()).countByTitleOrDescriptionContains(search);
        verify(itemRepository, never()).count();

        String key = generateCacheKey(search);
        verify(itemCacheService, times(1)).getItemCount(key);
        verify(itemCacheService, times(1)).putItemCount(key, totalCount);
    }
}
