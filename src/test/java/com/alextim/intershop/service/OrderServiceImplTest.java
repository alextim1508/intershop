package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.CurrentOrderAbsentException;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.listener.ApplicationReadyListener;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.alextim.intershop.utils.Status.COMPLETED;
import static com.alextim.intershop.utils.Status.CURRENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private ApplicationReadyListener listener;

    private Order currentOrder;
    private Item item;

    @BeforeEach
    public void setUp() {
        doNothing().when(listener).onApplicationEvent(any(ApplicationReadyEvent.class));

        item = new Item();
        item.setId(1L);
        item.setPrice(10.0);

        currentOrder = new Order();

        OrderItem orderItem = new OrderItem(currentOrder, item);
        orderItem.setCount(2);

        currentOrder.setOrderItems(new ArrayList<>(Collections.singletonList(orderItem)));
    }

    @Test
    void testSaveOrder() {
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.save(order);

        assertNotNull(savedOrder);
        verify(orderRepository).save(order);
    }

    @Test
    void testFindAllCompletedOrders() {
        Order order = new Order();
        order.setStatus(COMPLETED);
        when(orderRepository.findByStatus(COMPLETED)).thenReturn(Collections.singletonList(order));

        var completedOrders = orderService.findAllCompletedOrders();

        assertFalse(completedOrders.isEmpty());
        assertEquals(1, completedOrders.size());
        assertEquals(COMPLETED, completedOrders.get(0).getStatus());
    }

    @Test
    void testFindByIdWhenOrderExists() {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.findById(orderId);

        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testFindByIdWhenOrderDoesNotExist() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findById(orderId));
    }

    @Test
    void testGetCurrentOrderExists() {
        Order order = new Order();
        when(orderRepository.findByStatus(CURRENT)).thenReturn(Collections.singletonList(order));

        Order currentOrder = orderService.getCurrentOrder();

        assertNotNull(currentOrder);
        assertEquals(order, currentOrder);
    }

    @Test
    void testGetCurrentOrderAbsent() {
        when(orderRepository.findByStatus(CURRENT)).thenReturn(Collections.emptyList());

        assertThrows(CurrentOrderAbsentException.class, () -> orderService.getCurrentOrder());
    }

    @Test
    void testCalcPrice() {
        Item item = new Item();
        item.setPrice(10.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(2);

        Order order = new Order();
        order.setOrderItems(Collections.singletonList(orderItem));

        double price = orderService.calcPrice(order);

        assertEquals(20.0, price);
    }

}
