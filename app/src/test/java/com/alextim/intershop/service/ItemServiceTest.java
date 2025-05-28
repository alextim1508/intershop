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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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

        Mockito.when(itemRepository.save(ArgumentMatchers.any(Item.class))).thenReturn(Mono.just(item));
        Mockito.when(itemCacheService.putItem(ArgumentMatchers.any(Item.class))).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.save(item))
                .expectNext(item)
                .verifyComplete();

        Mockito.verify(itemRepository, Mockito.times(1)).save(item);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldFindItemWithQuantityWhenItemDoesNotExistInCacheByIdTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        Order order = new Order();
        order.setId(2L);

        OrderItem orderItem = new OrderItem(item.getId(), order.getId(), 5);

        Mockito.when(itemRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Mono.just(item));
        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(order));
        Mockito.when(orderItemRepository.findByItemIdAndOrderId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(orderItem));
        Mockito.when(itemCacheService.getItem(ArgumentMatchers.any(Long.class))).thenReturn(Mono.empty());
        Mockito.when(itemCacheService.putItem(ArgumentMatchers.any(Item.class))).thenReturn(Mono.just(true));


        StepVerifier.create(itemService.findItemWithQuantityById(item.getId()))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 5)
                .verifyComplete();

        Mockito.verify(itemRepository, Mockito.times(1)).findById(item.getId());
        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderItemRepository, Mockito.times(1)).findByItemIdAndOrderId(item.getId(), order.getId());
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemCacheService, Mockito.times(1)).getItem(item.getId());
        Mockito.verify(itemCacheService, Mockito.times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldGetItemFromCacheAndAddQuantityTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        Order order = new Order();
        order.setId(2L);

        OrderItem orderItem = new OrderItem(item.getId(), order.getId(), 5);

        Mockito.when(itemRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Mono.just(item));
        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(order));
        Mockito.when(orderItemRepository.findByItemIdAndOrderId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(orderItem));
        Mockito.when(itemCacheService.getItem(ArgumentMatchers.any(Long.class))).thenReturn(Mono.just(item));
        Mockito.when(itemCacheService.putItem(ArgumentMatchers.any(Item.class))).thenReturn(Mono.just(true));


        StepVerifier.create(itemService.findItemWithQuantityById(item.getId()))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 5)
                .verifyComplete();

        Mockito.verify(itemRepository, Mockito.never()).findById(ArgumentMatchers.anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderItemRepository, Mockito.times(1)).findByItemIdAndOrderId(item.getId(), order.getId());
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemCacheService, Mockito.times(1)).getItem(item.getId());
        Mockito.verify(itemCacheService, Mockito.times(1)).putItem(item);
    }

    @Test
    public void testFindItemWithQuantityById_shouldDoesNotFindItemByIdTest() {
        Item item = new Item("Item", "description", "img", 12.0);
        item.setId(1L);

        Mockito.when(itemRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Mono.empty());
        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(new Order()));
        Mockito.when(itemCacheService.getItem(ArgumentMatchers.anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(itemService.findItemWithQuantityById(1L))
                .expectError(ItemNotFoundException.class)
                .verify();

        Mockito.verify(itemRepository, Mockito.times(1)).findById(item.getId());
        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    public void findItemsWithQuantity_shouldFindItemsWithQuantityByEmptySearchStringAndNoSortWhenItemDoesNotExistInCacheTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Item1", "Description 1", "img", 100.0);
        item1.setId(1L);
        Item item2 = new Item("Item2", "Description 2", "img", 200.0);
        item2.setId(2L);

        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(currentOrder));
        Mockito.when(itemRepository.findAllBy(ArgumentMatchers.any(PageRequest.class))).thenReturn(Flux.just(item1, item2));
        Mockito.when(orderItemRepository.findByOrderId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), 1L, 10),
                        new OrderItem(currentOrder.getId(), 2L, 5)
                ));
        Mockito.when(itemCacheService.getItemList(ArgumentMatchers.anyString())).thenReturn(Flux.empty());
        Mockito.when(itemCacheService.putItemList(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 10)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 5)
                .verifyComplete();


        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemRepository, Mockito.times(1)).findAllBy(PageRequest.of(0, 10));
        Mockito.verify(orderItemRepository, Mockito.times(1)).findByOrderId(currentOrder.getId());
        String key = generateCacheKey("", SortType.NO, 0, 10);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemList(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldGetItemsFromCacheAndAddQuantityTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Item1", "Description 1", "img", 100.0);
        item1.setId(1L);
        Item item2 = new Item("Item2", "Description 2", "img", 200.0);
        item2.setId(2L);

        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(currentOrder));
        Mockito.when(itemRepository.findAllBy(ArgumentMatchers.any(PageRequest.class))).thenReturn(Flux.just(item1, item2));
        Mockito.when(orderItemRepository.findByOrderId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), 1L, 10),
                        new OrderItem(currentOrder.getId(), 2L, 5)
                ));
        Mockito.when(itemCacheService.getItemList(ArgumentMatchers.anyString())).thenReturn(Flux.just(item1, item2));
        Mockito.when(itemCacheService.putItemList(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 10)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 5)
                .verifyComplete();


        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemRepository, Mockito.never()).findAllBy(ArgumentMatchers.any(PageRequest.class));
        Mockito.verify(orderItemRepository, Mockito.times(1)).findByOrderId(currentOrder.getId());
        String key = generateCacheKey("", SortType.NO, 0, 10);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemList(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldFindItemsWithQuantityBySearchStringAndSortWhenItemDoesNotExistInCacheTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item1 = new Item("Apple", "Fresh apple", "img", 50.0);
        item1.setId(1L);
        Item item2 = new Item("Banana", "Yellow banana", "img", 30.0);
        item2.setId(2L);

        Mockito.when(orderRepository.findByStatus(ArgumentMatchers.any(Status.class))).thenReturn(Flux.just(currentOrder));

        Mockito.when(orderItemRepository.findByOrderId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(currentOrder.getId(), item1.getId(), 7)
                ));
        Mockito.when(itemRepository.findByTitleOrDescriptionContains(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(Flux.just(item1, item2));
        Mockito.when(itemCacheService.getItemList(ArgumentMatchers.anyString())).thenReturn(Flux.empty());
        Mockito.when(itemCacheService.putItemList(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("test", SortType.ALPHA, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item1) && entry.getValue() == 7)
                .expectNextMatches(entry ->
                        entry.getKey().equals(item2) && entry.getValue() == 0)
                .verifyComplete();

        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderRepository, Mockito.never()).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemRepository, Mockito.times(1)).findByTitleOrDescriptionContains("test",
                PageRequest.of(0, 10, Sort.by("title").ascending()));

        String key = generateCacheKey("test", SortType.ALPHA, 0, 10);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemList(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemList(key, Arrays.asList(item1, item2));
    }

    @Test
    public void findItemsWithQuantity_shouldCreateNewOrderThenFindItemsWithQuantityTest() {
        Order newOrder = new Order();
        newOrder.setId(1L);

        Item item = new Item("Item", "Description", "img", 100.0);
        item.setId(1L);

        Mockito.when(orderRepository.findByStatus(CURRENT)).thenReturn(Flux.empty());
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(Mono.just(newOrder));
        Mockito.when(itemRepository.findAllBy(ArgumentMatchers.any())).thenReturn(Flux.just(item));
        Mockito.when(orderItemRepository.findByOrderId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(
                        new OrderItem(newOrder.getId(), item.getId(), 3)
                ));
        Mockito.when(itemCacheService.getItemList(ArgumentMatchers.anyString())).thenReturn(Flux.empty());
        Mockito.when(itemCacheService.putItemList(ArgumentMatchers.anyString(), ArgumentMatchers.anyList())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.findItemsWithQuantity("", SortType.NO, 0, 10))
                .expectNextMatches(entry ->
                        entry.getKey().equals(item) && entry.getValue() == 3)
                .verifyComplete();

        Mockito.verify(orderRepository, Mockito.times(1)).findByStatus(CURRENT);
        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any(Order.class));
        Mockito.verify(itemRepository, Mockito.times(1)).findAllBy(PageRequest.of(0, 10));
        Mockito.verify(orderItemRepository, Mockito.times(1)).findByOrderId(newOrder.getId());

        String key = generateCacheKey("", SortType.NO, 0, 10);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemList(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemList(key, Arrays.asList(item));
    }

    @Test
    public void count_shouldGetCountItemsBySearchEmptyStringTest() {
        String search = "";
        long totalCount = 10L;

        Mockito.when(itemRepository.count()).thenReturn(Mono.just(totalCount));
        Mockito.when(itemCacheService.getItemCount(ArgumentMatchers.anyString())).thenReturn(Mono.empty());
        Mockito.when(itemCacheService.putItemCount(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        Mockito.verify(itemRepository).count();
        Mockito.verify(itemRepository, Mockito.never()).countByTitleOrDescriptionContains(ArgumentMatchers.anyString());

        String key = generateCacheKey(search);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemCount(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemCount(key, totalCount);
    }

    @Test
    public void count_shouldGetCountItemsBySearchStringTest() {
        String search = "test";
        long totalCount = 10L;

        Mockito.when(itemRepository.countByTitleOrDescriptionContains(ArgumentMatchers.anyString())).thenReturn(Mono.just(totalCount));
        Mockito.when(itemCacheService.getItemCount(ArgumentMatchers.anyString())).thenReturn(Mono.empty());
        Mockito.when(itemCacheService.putItemCount(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        Mockito.verify(itemRepository).countByTitleOrDescriptionContains(search);
        Mockito.verify(itemRepository, Mockito.never()).count();

        String key = generateCacheKey(search);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemCount(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemCount(key, totalCount);
    }

    @Test
    public void count_shouldGetCountItemsBySearchStringFromCacheTest() {
        String search = "test";
        long totalCount = 10L;

        Mockito.when(itemRepository.countByTitleOrDescriptionContains(ArgumentMatchers.anyString())).thenReturn(Mono.just(totalCount));
        Mockito.when(itemCacheService.getItemCount(ArgumentMatchers.anyString())).thenReturn(Mono.just(totalCount));
        Mockito.when(itemCacheService.putItemCount(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(true));

        StepVerifier.create(itemService.count(search))
                .expectNext(totalCount)
                .verifyComplete();

        Mockito.verify(itemRepository, Mockito.never()).countByTitleOrDescriptionContains(search);
        Mockito.verify(itemRepository, Mockito.never()).count();

        String key = generateCacheKey(search);
        Mockito.verify(itemCacheService, Mockito.times(1)).getItemCount(key);
        Mockito.verify(itemCacheService, Mockito.times(1)).putItemCount(key, totalCount);
    }
}
