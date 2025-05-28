package com.alextim.intershop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @GetMapping
    public Mono<String> home() {
        return Mono.just("redirect:/main/items");
    }
}
