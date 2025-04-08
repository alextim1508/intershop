package com.alextim.intershop.service;


import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Action;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order save(Order order);

    List<Order> findAllCompletedOrders();

    Order findById(long id);

    Order getCurrentOrder();

    Map<Item, Integer> getItemsFromOrder(Order order);

    double calcPrice(Order order);

    Order completeCurrentOrder();

    void changeItemCountInCart(long id, Action action);
}
