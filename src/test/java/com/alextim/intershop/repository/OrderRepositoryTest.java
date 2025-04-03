package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.utils.Status;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void createOrderTest() {
        Item savedItem = itemRepository.save(new Item("title", "description", "url", 1.2));

        Order order = new Order();
        order.addItem(savedItem);

        Order savedOrder = orderRepository.save(order);

        entityManager.clear();

        Optional<Order> orderById = orderRepository.findById(savedOrder.getId());
        Assertions.assertTrue(orderById.isPresent());

        List<OrderItem> orderItems = orderById.get().getOrderItems();
        Assertions.assertEquals(1, orderItems.size());
        OrderItem orderItem = orderItems.get(0);
        Assertions.assertEquals(savedOrder, orderItem.getOrder());
        Assertions.assertEquals(savedItem, orderItem.getItem());

    }

    @Test
    public void findCurrentAndCompletedOrdersTest() {
        Item savedItem1 = itemRepository.save(new Item("title1", "description", "url", 1.2));
        Order completedOrder = new Order();
        completedOrder.addItem(savedItem1);
        completedOrder.setStatus(Status.COMPLETED);
        Order savedCompletedOrder1 = orderRepository.save(completedOrder);

        Item savedItem2 = itemRepository.save(new Item("title2", "description", "url", 1.2));
        Order order = new Order();
        order.addItem(savedItem2);
        Order savedCurrentOrder = orderRepository.save(order);

        entityManager.clear();

        List<Order> completedOrders = orderRepository.findByStatus(Status.COMPLETED);
        Assertions.assertEquals(1, completedOrders.size());
        Assertions.assertTrue(completedOrders.contains(savedCompletedOrder1));
        Assertions.assertEquals(savedItem1, completedOrders.get(0).getOrderItems().get(0).getItem());


        Optional<Order> currentOrders = orderRepository.findCurrentOrder();
        Assertions.assertTrue(currentOrders.isPresent());
        Assertions.assertEquals(savedCurrentOrder, currentOrders.get());
        Assertions.assertEquals(savedItem2,  currentOrders.get().getOrderItems().get(0).getItem());
    }

}
