package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ActionDto;
import com.alextim.intershop.mapper.ActionMapper;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;

    @GetMapping("/{id}")
    public Mono<Rendering> getItem(@PathVariable("id") long id) {
        log.info("incoming request for getting item by id {}", id);

        return itemService.findItemWithQuantityById(id)
                .map(item -> itemMapper.toDto(item.getKey(), item.getValue()))
                .doOnNext(itemDto -> log.info("item dto: {}", itemDto))
                .map(dto -> Rendering.view("item")
                        .modelAttribute("item", dto)
                        .build()
                );
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@PathVariable int id, @ModelAttribute ActionDto action) {
        log.info("incoming request for change item quantity in cart. item id {}, action {}",
                id, action);

        return orderService.changeItemQuantityInCart(id, actionMapper.to(action))
               .flatMap(order -> Mono.just("redirect:/items/" + id));
    }
}
