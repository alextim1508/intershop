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

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/main/items")
@Slf4j
public class MainController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @Value("${app.partition-count}")
    private int partitionCount;

    @GetMapping
    public String getItems(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "NO") SortType sort,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "1") Integer pageNumber,
                           Model model) {
        log.info("incoming request for getting items");

        Map<Item, Integer> itemCounts = itemService.search(search, sort, pageNumber - 1, pageSize);

        List<ItemDto> itemDtos = itemCounts.entrySet().stream()
                .map(it -> itemMapper.toDto(it.getKey(), it.getValue()))
                .toList();

        List<List<ItemDto>> partitionItemDto = Lists.partition(itemDtos, partitionCount);
        log.info("item dto: {}", partitionItemDto);

        long count = itemService.count(search);

        PagingDto pagingDto = new PagingDto(
                pageNumber,
                pageSize,
                (long) pageNumber * pageSize < count,
                pageNumber != 1);
        log.info("paging dto: {}", pagingDto);

        model.addAttribute("items", partitionItemDto);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort.name());
        model.addAttribute("paging", pagingDto);

        return "main";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable long id,
                                        @RequestParam Action action,
                                        @RequestParam String search,
                                        @RequestParam SortType sort,
                                        @RequestParam Integer pageSize,
                                        @RequestParam Integer pageNumber) {
        log.info("incoming request for change item count in cart. item id {}, action {}", id, action);

        orderService.changeItemCountInCart(id, action);

        return "redirect:/main/items" +
                "?" +
                "search=" + search + "&" +
                "sort=" + sort + "&" +
                "pageSize=" + pageSize + "&" +
                "pageNumber=" + pageNumber;
    }
}
