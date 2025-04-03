package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.dto.PagingDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.SortType;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/main/items")
@Slf4j
public class MainController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @Value("app.partitionCount")
    private int partitionCount;

    @GetMapping
    public String getItems(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "NO") SortType sort,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "1") Integer pageNumber,
                           Model model) {
        log.info("incoming request for getting items");

        List<Item> items = itemService.search(search, sort, pageNumber, pageSize);

        List<List<ItemDto>> itemDto = Lists.partition(
                items.stream().map(itemMapper::toDto).toList(),
                Math.min(partitionCount, pageSize)
        );
        log.info("item dto: {}", itemDto);

        long count = itemService.count();

        PagingDto pagingDto = new PagingDto(pageNumber,
                pageSize,
                (long) pageNumber * pageSize < count,
                pageNumber != 0);
        log.info("paging dto: {}", pagingDto);

        model.addAttribute("items", itemDto);
        model.addAttribute("search",search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", pagingDto);

        return "main";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable long id, @RequestParam Action action) {
        log.info("incoming request for change item count in cart. item id {}, action {}", id, action);

        orderService.changeItemCountInCart(id, action);

        return "redirect:/main/items";
    }
}
