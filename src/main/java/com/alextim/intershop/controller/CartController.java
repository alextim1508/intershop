package com.alextim.intershop.controller;

import com.alextim.intershop.dto.CartDto;
import com.alextim.intershop.utils.Action;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/cart/items")
public class CartController {

    @GetMapping
    public String getCart(Model model) {
        CartDto cartDto = new CartDto();

        model.addAttribute("items", cartDto.items);
        model.addAttribute("total", cartDto.total);
        model.addAttribute("empty", cartDto.empty);
        return "cart";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable int id, @RequestParam Action action) {

        return "redirect:/cart/items";
    }
 }
