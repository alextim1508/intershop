package com.alextim.intershop.controller;

import com.alextim.intershop.dto.CartDto;
import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Controller
@RequestMapping(value = "/cart/items")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @GetMapping
    public String getCart(Model model) {
        log.info("incoming request for getting current order");

        Order currentOrder = orderService.getCurrentOrder();

        Map<Item, Integer> items = orderService.getItemsFromOrder(currentOrder);

        List<ItemDto> dto = items.entrySet().stream()
                .map(it -> itemMapper.toDto(it.getKey(), it.getValue())).toList();
        log.info("dto: {}", dto);

        double price = orderService.calcPrice(currentOrder);

        CartDto cartDto = new CartDto(
                dto,
                price,
                dto.isEmpty());
        log.info("cartDto: {}", cartDto);

        model.addAttribute("items", cartDto.items);
        model.addAttribute("total", cartDto.total);
        model.addAttribute("empty", cartDto.empty);

        return "cart";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable long id, @RequestParam Action action) {
        log.info("incoming request for change item count in cart. item id {}, action {}", id, action);

        orderService.changeItemCountInCart(id, action);

        return "redirect:/cart/items";
    }
 }
