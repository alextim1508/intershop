package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Action;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();

    Order findById(long id);

    List<Item> getItemsFromCurrentOrder();

    List<Item> getItemsFromOrder(Order order);

    double calcPriceOfCurrentOrder();

    Order completeCurrentOrder();

    void changeItemCountInCart(long id, Action action);
}
