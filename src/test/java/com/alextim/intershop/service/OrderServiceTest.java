package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

import static com.alextim.intershop.utils.Status.COMPLETED;
import static com.alextim.intershop.utils.Status.CURRENT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {OrderServiceImpl.class})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private ItemRepository itemRepository;
    @MockitoBean
    private OrderRepository orderRepository;
    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @Test
    void save_shouldCreateOrderTest() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));

        StepVerifier.create(orderService.save(order))
                .expectNext(order)
                .verifyComplete();

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void findByStatus_shouldFindAllCompletedOrdersTest() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(COMPLETED);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(COMPLETED);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(order1, order2));

        StepVerifier.create(orderService.findAllCompletedOrders())
                .expectNext(order1, order2)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(COMPLETED);
    }

    @Test
    public void findById_shouldFindOrderByIdTest() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Mono.just(order));

        StepVerifier.create(orderService.findById(1L))
                .expectNext(order)
                .verifyComplete();

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldDoesNotFindOrderByIdTest() {
        when(orderRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.findById(1L))
                .expectErrorSatisfies(e -> assertThat(e).isInstanceOf(OrderNotFoundException.class))
                .verify();
    }

    @Test
    void findCurrentOrder_shouldFindCurrentOrderThenDoesNotCreateNewOrderTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Order newOrder = new Order();
        newOrder.setId(2L);

        when(orderRepository.save(any())).thenReturn(Mono.just(newOrder));
        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));

        StepVerifier.create(orderService.findCurrentOrder())
                .expectNext(currentOrder)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void findCurrentOrder_shouldDoesNotFindCurrentOrderThenCreateNewOrderTest() {
        Order newOrder = new Order();
        newOrder.setId(1L);

        when(orderRepository.save(any())).thenReturn(Mono.just(newOrder));
        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.empty());

        StepVerifier.create(orderService.findCurrentOrder())
                .expectNext(newOrder)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    public void findItemsWithQuantityByOrderId_shouldFindItemsWithQuantityByOrderIdSuccessfullyTest() {
        Order order = new Order();
        order.setId(1L);

        Item item1 = new Item("Item1", "description", "img", 12.0);
        item1.setId(1L);

        Item item2 = new Item("Item2", "description", "img", 14.0);
        item2.setId(2L);

        List<OrderItem> orderItems = Arrays.asList(
                new OrderItem(order.getId(), item1.getId(), 10),
                new OrderItem(order.getId(), item2.getId(), 20)
        );

        when(orderRepository.findById(anyLong())).thenReturn(Mono.just(order));
        when(orderItemRepository.findByOrderId(anyLong())).thenReturn(Flux.fromIterable(orderItems));
        when(itemRepository.findAllById(anySet())).thenReturn(Flux.just(item1, item2));

        StepVerifier.create(orderService.findItemsWithQuantityByOrderId(order.getId()))
                .expectNextMatches(entry ->
                        entry.equals(new SimpleImmutableEntry<>(item1, 10)))
                .expectNextMatches(entry ->
                        entry.equals(new SimpleImmutableEntry<>(item2, 20)))
                .verifyComplete();

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderItemRepository, times(1)).findByOrderId(order.getId());
        verify(itemRepository, times(1)).findAllById(Set.of(item1.getId(), item2.getId()));
    }

    @Test
    public void findItemsWithQuantityByOrderId_shouldDoesNotFindItemsTest() {
        when(orderRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.findItemsWithQuantityByOrderId(1L))
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    public void completeCurrentOrder_shouldCompleteCurrentOrderTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);
        currentOrder.setStatus(CURRENT);

        Order completedOrder = new Order();
        completedOrder.setId(1L);
        completedOrder.setStatus(COMPLETED);
        completedOrder.setCompleted(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));
        when(orderRepository.save(currentOrder)).thenReturn(Mono.just(completedOrder));

        StepVerifier.create(orderService.completeCurrentOrder())
                .expectNext(completedOrder)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, times(1)).save(currentOrder);
    }

    @Test
    public void completeCurrentOrder_shouldCreateNewOrderThenCompleteItTest() {
        Order newOrder = new Order();
        newOrder.setId(2L);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(newOrder));

        StepVerifier.create(orderService.completeCurrentOrder())
                .expectNext(newOrder)
                .verifyComplete();

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    public void changeItemQuantityInCart_shouldAddQuantityToExistingOrderItemTest() {
        Order currentOrder = new Order();
        currentOrder.setId(1L);

        Item item = new Item("Item", "description", "img", 14.0);
        item.setId(1L);

        OrderItem orderItem = new OrderItem(currentOrder.getId(), item.getId(), 3);
        OrderItem updatedOderItem = new OrderItem(orderItem.getOrderId(), orderItem.getItemId(), orderItem.getQuantity() + 1);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(currentOrder));
        when(orderItemRepository.findByItemIdAndOrderId(anyLong(), anyLong())).thenReturn(Mono.just(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(Mono.just(updatedOderItem));

        StepVerifier.create(orderService.changeItemQuantityInCart(item.getId(), Action.PLUS))
                .expectNextMatches(it -> it.getQuantity() == orderItem.getQuantity())
                .verifyComplete();

        assertThat(orderItem).extracting(OrderItem::getQuantity).isEqualTo(updatedOderItem.getQuantity());

        verify(orderRepository, times(1)).findByStatus(CURRENT);
        verify(orderRepository, times(0)).save(any(Order.class));

        verify(orderItemRepository, times(1)).findByItemIdAndOrderId(item.getId(), currentOrder.getId());
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderItemRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void changeItemQuantityInCart_shouldDeleteOrderItemTest() {
        Order order = new Order();
        order.setId(1L);

        Item item = new Item("Item", "description", "img", 14.0);
        item.setId(1L);

        OrderItem orderItem = new OrderItem(order.getId(), item.getId(), 5);
        orderItem.setId(1L);

        when(orderRepository.findByStatus(any(Status.class))).thenReturn(Flux.just(order));
        when(orderItemRepository.findByItemIdAndOrderId(item.getId(), order.getId())).thenReturn(Mono.just(orderItem));
        when(orderItemRepository.deleteById(orderItem.getId())).thenReturn(Mono.empty());

        StepVerifier.create(orderService.changeItemQuantityInCart(item.getId(), Action.DELETE))
                .verifyComplete();

        verify(orderRepository).findByStatus(CURRENT);
        verify(orderItemRepository).findByItemIdAndOrderId(item.getId(), order.getId());
        verify(orderItemRepository).deleteById(orderItem.getId());
    }

    @Test
    public void calcPrice_shouldCalculatePriceTest() {
        Item item = new Item("Item", "description", "img", 14.0);
        int quantity = 2;

        assertThat(orderService.calcPrice(item, quantity))
                .isEqualTo(item.getPrice() * quantity);
    }
}