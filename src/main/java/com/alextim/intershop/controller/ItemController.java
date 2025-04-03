package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.utils.Action;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/items")
public class ItemController {

    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") int id, Model model) {

        ItemDto itemDto = new ItemDto();
        model.addAttribute("item", itemDto);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable int id, @RequestParam Action action) {

        return "redirect:/items/" + id;
    }
}
