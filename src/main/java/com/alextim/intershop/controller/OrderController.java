package com.alextim.intershop.controller;

import com.alextim.intershop.dto.OrderDto;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @GetMapping
    public String getOrders(Model model) {
        log.info("incoming request for getting orders");

        List<Order> orders = orderService.findAllCompletedOrders();

        List<OrderDto> dto = orders.stream().map(order ->
                new OrderDto(
                        order.getId(),
                        orderService.getItemsFromOrder(order).entrySet().stream()
                                .map(it -> itemMapper.toDto(it.getKey(), it.getValue())).toList(),
                        orderService.calcPrice(order))
        ).toList();
        log.info("order dto: {}", dto);

        model.addAttribute("orders", dto);

        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable long id, @RequestParam(defaultValue = "false") boolean newOrder, Model model) {
        log.info("incoming request for getting order by id: {}", id);

        Order order = orderService.findById(id);

        OrderDto dto = new OrderDto(
                order.getId(),
                orderService.getItemsFromOrder(order).entrySet().stream()
                        .map(it -> itemMapper.toDto(it.getKey(), it.getValue())).toList(),
                orderService.calcPrice(order)
        );
        log.info("order dto: {}", dto);

        model.addAttribute("order", dto);
        model.addAttribute("newOrder", newOrder);

        return "order";
    }

}
