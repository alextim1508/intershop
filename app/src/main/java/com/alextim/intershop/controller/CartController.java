package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ActionDto;
import com.alextim.intershop.dto.CartDto;
import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.mapper.ActionMapper;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


@Controller
@RequestMapping(value = "/cart/items")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final OrderService orderService;

    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;

    @GetMapping
    public Mono<Rendering> getCart() {
        log.info("incoming request for getting current order");

        return orderService.findCurrentOrder()
                .flatMap(this::getCartDtoMono)
                .doOnNext(cartDto -> log.info("cartDto: {}", cartDto))
                .map(cartDto -> Rendering.view("cart")
                        .modelAttribute("items", cartDto.items)
                        .modelAttribute("total", cartDto.total)
                        .modelAttribute("empty", cartDto.empty)
                        .build()
                );
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@PathVariable long id, @ModelAttribute ActionDto action) {
        log.info("incoming request for change item quantity in cart. item id {}, action {}", id, action);

        return orderService.changeItemQuantityInCart(id, actionMapper.to(action))
                .then(Mono.just("redirect:/cart/items"));
    }

    private Mono<CartDto> getCartDtoMono(Order order) {
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

                    return new CartDto(itemDtos, totalSum, itemDtos.isEmpty());
                });
    }
}