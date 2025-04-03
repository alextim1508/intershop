package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.CurrentOrderAbsentException;
import com.alextim.intershop.exeption.ItemAbsentInCurrentOrderException;
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
import java.util.List;

import static com.alextim.intershop.utils.Status.COMPLETED;
import static com.alextim.intershop.utils.Status.CURRENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<Order> findAllOrders() {
        List<Order> orders = orderRepository.findByStatus(COMPLETED);
        log.info("completed orders: {}", orders);

        return orders;
    }

    @Override
    public Order findById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        log.info("order by id {}: {}", id, order);

        return order;
    }

    @Override
    public List<Item> getItemsFromCurrentOrder() {
        Order currentOrder = orderRepository.findByStatus(CURRENT).stream().findFirst()
                .orElseThrow(CurrentOrderAbsentException::new);
        log.info("current order: {}", currentOrder);

        List<Item> items = currentOrder.getOrderItems().stream().map(OrderItem::getItem).toList();
        log.info("items: {}", items);

        return items;
    }

    @Override
    public List<Item> getItemsFromOrder(Order order) {
        List<Item> items = order.getOrderItems().stream().map(OrderItem::getItem).toList();
        log.info("items: {}", items);

        return items;
    }

    @Override
    public double calcPriceOfCurrentOrder() {
        Order currentOrder = orderRepository.findByStatus(CURRENT).stream().findFirst()
                .orElseThrow(CurrentOrderAbsentException::new);
        log.info("current order: {}", currentOrder);

        double price = 0;
        for (OrderItem orderItem : currentOrder.getOrderItems()) {
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

        OrderItem orderItem = currentOrder.getOrderItems().stream()
                .filter(it -> it.getItem().equals(item)).findFirst()
                .orElseThrow(() -> new ItemAbsentInCurrentOrderException(item.getId()));
        log.info("order-item relationship: {}", orderItem);

        switch (action) {
            case PLUS -> orderItem.setCount(orderItem.getCount() + 1);
            case MINUS -> orderItem.setCount(orderItem.getCount() - 1);
            case DELETE -> currentOrder.getOrderItems().remove(orderItem);
        }

        orderRepository.save(currentOrder);
        log.info("{} is saved", currentOrder);
    }
}
