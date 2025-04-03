package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @GetMapping("/{id}")
    public String getItem(@PathVariable("id") long id, Model model) {
        log.info("incoming request for getting item by id {}", id);
        Item item = itemService.findById(id);

        ItemDto dto = itemMapper.toDto(item);
        log.info("item dto: {}", dto);

        model.addAttribute("item", dto);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable int id, @RequestParam Action action) {
        log.info("incoming request for change item count in cart. item id {}, action {}", id, action);

        orderService.changeItemCountInCart(id, action);

        return "redirect:/items/" + id;
    }
}
