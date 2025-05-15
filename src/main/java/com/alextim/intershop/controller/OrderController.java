package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.dto.OrderDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @GetMapping
    public Mono<Rendering> getOrders() {
        log.info("incoming request for getting orders");

        return orderService.findAllCompletedOrders()
                .flatMap(this::getOrderDtoMono)
                .doOnNext(orderDto -> log.info("orderDto: {}", orderDto))
                .collectList()
                .map(orderDto -> Rendering.view("orders")
                        .modelAttribute("orders", orderDto)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public Mono<Rendering> getOrder(@PathVariable long id, @RequestParam(defaultValue = "false") boolean newOrder) {
        log.info("incoming request for getting order by id: {}", id);

        return orderService.findById(id)
                .flatMap(this::getOrderDtoMono)
                .doOnNext(orderDto -> log.info("orderDto: {}", orderDto))
                .map(orderDto -> Rendering.view("order")
                        .modelAttribute("order", orderDto)
                        .modelAttribute("newOrder", newOrder)
                        .build()
                );
    }

    private Mono<OrderDto> getOrderDtoMono(Order order) {
        return orderService.findItemsWithQuantityByOrderId(order.getId())
                .collectMap(Entry::getKey, Entry::getValue)
                .map(map -> {
                    List<ItemDto> itemDtos = new ArrayList<>();
                    double totalSum = 0;
                    for (Entry<Item, Integer> entry : map.entrySet()) {
                        itemDtos.add(itemMapper.toDto(entry.getKey(), entry.getValue()));
                        totalSum += orderService.calcPrice(entry.getKey(), entry.getValue());
                    }
                    log.info("total sum: {} of order {}", totalSum, order);

                    return new OrderDto(order.getId(), itemDtos, totalSum);
                });
    }
}