package com.alextim.intershop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/buy")
public class BuyController {

    @PostMapping
    public String buy() {
        int orderId = 0;

        return "redirect:/orders/" + orderId + "?newOrder=true";
    }
}
