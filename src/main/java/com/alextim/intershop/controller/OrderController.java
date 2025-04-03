package com.alextim.intershop.controller;

import com.alextim.intershop.dto.OrderDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public String getOrders(Model model) {

        List<OrderDto> orderDtos = new ArrayList<>();

        model.addAttribute("orders", orderDtos);

        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable int id, @RequestParam boolean newOrder, Model model) {
        OrderDto orderDto = new OrderDto();

        model.addAttribute("order", orderDto);
        model.addAttribute("newOrder", newOrder);
        return "order";
    }

}
