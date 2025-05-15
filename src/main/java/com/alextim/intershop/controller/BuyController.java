package com.alextim.intershop.controller;

import com.alextim.intershop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/buy")
@RequiredArgsConstructor
@Slf4j
public class BuyController {

    private final OrderService orderService;

    @PostMapping
    public Mono<String> buy() {
        log.info("incoming request for buying");

        return orderService.completeCurrentOrder()
                .flatMap(completedOrder -> Mono.just("redirect:/orders/" + completedOrder.getId() +
                        "?" +
                        "newOrder=" + true)
                );
    }
}
