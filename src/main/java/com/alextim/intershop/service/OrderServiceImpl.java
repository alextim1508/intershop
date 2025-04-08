package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.CurrentOrderAbsentException;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.exeption.OrderNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.utils.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alextim.intershop.utils.Status.COMPLETED;
import static com.alextim.intershop.utils.Status.CURRENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Override
    public Order save(Order order) {
        log.info("saving order: {}", order);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllCompletedOrders() {
        log.info("find all completed order");

        List<Order> orders = orderRepository.findByStatus(COMPLETED);
        log.info("completed orders: {}", orders);

        return orders;
    }

    @Override
    public Order findById(long id) {
        log.info("find order by id {}", id);

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        log.info("order: {}", order);

        return order;
    }

    @Override
    public Order getCurrentOrder() {
        Order currentOrder = orderRepository.findByStatus(CURRENT).stream()
                .findFirst()
                .orElseThrow(CurrentOrderAbsentException::new);
        log.info("current order: {}", currentOrder);

        return currentOrder;
    }

    @Override
    public Map<Item, Integer> getItemsFromOrder(Order order) {
        Map<Item, Integer> itemCounts = order.getOrderItems().stream()
                .collect(Collectors.toMap(
                        OrderItem::getItem,
                        OrderItem::getCount,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new));
        log.info("item counts: {}", itemCounts);

        return itemCounts;
    }

    @Override
    public double calcPrice(Order order) {
        log.info("price calculation");

        double price = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            log.info("{}", orderItem.getItem());

            Double itemPrice = orderItem.getItem().getPrice();
            log.info("item price: {}", itemPrice);

            int count = orderItem.getCount();
            log.info("count: {}", count);

            price +=  itemPrice * count;
        }

        log.info("total price: {}", price);

        return price;
    }

    @Override
    public Order completeCurrentOrder() {
        log.info("complete current order");

        Order currentOrder = orderRepository.findByStatus(CURRENT).stream().findFirst()
                .orElseThrow(CurrentOrderAbsentException::new);
        currentOrder.setStatus(COMPLETED);
        currentOrder.setCompleted(ZonedDateTime.now(ZoneId.of("Europe/Moscow")));

        orderRepository.save(currentOrder);
        log.info("complete current order: {}", currentOrder);

        Order newCurrentOrder = orderRepository.save(new Order());
        log.info("new current order: {}", newCurrentOrder);

        return currentOrder;
    }

    @Override
    public void changeItemCountInCart(long id, Action action) {
        log.info("changeItemCountInCart. id {}, action: {}", id, action);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        log.info("item by id: {}", item);

        Order currentOrder = orderRepository.findByStatus(CURRENT).stream().findFirst()
                .orElseThrow(CurrentOrderAbsentException::new);
        log.info("current order: {}", currentOrder);

        Optional<OrderItem> orderItem = currentOrder.getOrderItems().stream()
                .filter(it -> it.getItem().equals(item)).findFirst();
        log.info("order-item relationship: {}", orderItem);

        switch (action) {
            case PLUS -> {
                if(orderItem.isPresent()) {
                    orderItem.get().setCount(orderItem.get().getCount() + 1);
                } else {
                    currentOrder.getOrderItems().add(new OrderItem(currentOrder, item));
                }
            }
            case MINUS -> {
                orderItem.ifPresent(value -> {
                    if (value.getCount() > 0)
                        value.setCount(value.getCount() - 1);
                });
            }
            case DELETE -> {
                orderItem.ifPresent(value -> {
                    currentOrder.getOrderItems().remove(value);
                });
            }
        }

        orderRepository.save(currentOrder);
        log.info("{} is saved", currentOrder);
    }
}
