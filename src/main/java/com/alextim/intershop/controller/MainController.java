package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.dto.PagingDto;
import com.alextim.intershop.utils.Action;
import com.alextim.intershop.utils.SortType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/main/items")
public class MainController {

    @GetMapping
    public String getItems(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "NO") SortType sort,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "1") Integer pageNumber,
                           Model model) {

        List<List<ItemDto>> items = new ArrayList<>();
        PagingDto pagingDto = new PagingDto();

        model.addAttribute("items", items);
        model.addAttribute("search",search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", pagingDto);

        return "main";
    }

    @PostMapping("/{id}")
    public String changeItemCountInCart(@PathVariable int id, @RequestParam Action action) {

        return "redirect:/main/items";
    }
}
