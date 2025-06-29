package com.alextim.intershop.repository;

import com.alextim.intershop.AbstractRepoTestContainer;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemRepositoryTest extends AbstractRepoTestContainer {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    public void findByOrderId_shouldFindOrderItemsByOrderId() {
        User user = userRepository.save(new User("user", "pass")).block();

        List<Item> items = itemRepository.saveAll(Arrays.asList(
                        new Item("title1", "description1", "img", 0.0),
                        new Item("title2", "description2", "img", 0.0),
                        new Item("title3", "description3", "img", 0.0))
                )
                .thenMany(itemRepository.findAll())
                .collectList()
                .block();

        List<Order> orders = orderRepository.saveAll(Arrays.asList(new Order(user.getId()), new Order(user.getId())))
                .thenMany(orderRepository.findAll())
                .collectList()
                .block();

        Iterable<OrderItem> orderItems = orderItemRepository.saveAll(Arrays.asList(
                        new OrderItem(orders.get(0).getId(), items.get(0).getId(), 1),
                        new OrderItem(orders.get(0).getId(), items.get(1).getId(), 2),
                        new OrderItem(orders.get(1).getId(), items.get(2).getId(), 2))
                )
                .thenMany(orderItemRepository.findByOrderId(orders.get(0).getId()))
                .toIterable();

        Assertions.assertThat(orderItems)
                .withFailMessage("OrderItems is empty")
                .isNotEmpty()
                .withFailMessage("Size of OrderItems is not 2")
                .hasSize(2)
                .withFailMessage("Id is unexpected")
                .extracting(OrderItem::getItemId)
                .contains(items.get(0).getId(), items.get(1).getId());
    }

    @Test
    public void findByItemIdAndOrderId_shouldFindOrderItemByItemIdAndOrderId() {
        User user = userRepository.save(new User("user", "pass")).block();

        List<Item> items = itemRepository.saveAll(Arrays.asList(
                        new Item("title1", "description1", "img", 0.0),
                        new Item("title2", "description2", "img", 0.0),
                        new Item("title3", "description3", "img", 0.0))
                )
                .thenMany(itemRepository.findAll())
                .collectList()
                .block();

        List<Order> orders = orderRepository.saveAll(Arrays.asList(new Order(user.getId()), new Order(user.getId())))
                .thenMany(orderRepository.findAll())
                .collectList()
                .block();

        OrderItem orderItem = orderItemRepository.saveAll(Arrays.asList(
                        new OrderItem(orders.get(0).getId(), items.get(0).getId(), 1),
                        new OrderItem(orders.get(0).getId(), items.get(1).getId(), 2),
                        new OrderItem(orders.get(1).getId(), items.get(2).getId(), 3))
                )
                .then(orderItemRepository.findByItemIdAndOrderId(items.get(0).getId(), orders.get(0).getId()))
                .block();

        assertThat(orderItem)
                .withFailMessage("OrderItem is null")
                .isNotNull()
                .withFailMessage("Quantity is unexpected")
                .extracting(OrderItem::getQuantity)
                .isEqualTo(1);
    }
}
