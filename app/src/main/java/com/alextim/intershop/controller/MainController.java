package com.alextim.intershop.controller;

import com.alextim.intershop.dto.ActionDto;
import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.dto.PagingDto;
import com.alextim.intershop.dto.ViewParamDto;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.mapper.ActionMapper;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.ItemService;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.utils.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.alextim.intershop.utils.Utils.extractUserId;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/main/items")
@Slf4j
public class MainController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;

    @Value("${app.partition-count}")
    private int partitionCount;

    @GetMapping
    public Mono<Rendering> getItems(@AuthenticationPrincipal User user,
                                    @RequestParam(defaultValue = "") String search,
                                    @RequestParam(defaultValue = "NO") SortType sort,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "1") Integer pageNumber) {
        log.info("incoming request for getting items from user {}. search: \"{}\", sort: {}, pageNumber: {}, pageSize: {}",
                user, search, sort, pageNumber, pageSize);

        Flux<List<ItemDto>> partitionItemDto =
                itemService.findItemsWithQuantity(extractUserId(user), search, sort, pageNumber - 1, pageSize)
                    .map(entry -> itemMapper.toDto(entry.getKey(), entry.getValue()))
                    .doOnNext(itemDto -> log.info("itemDto: {}", itemDto.getId()))
                    .buffer(partitionCount)
                    .doOnNext(it -> log.info("partitioned item dtos: {}", it));

        Mono<PagingDto> pagingDto = itemService.count(search)
                .map(count ->
                        new PagingDto(
                                pageNumber,
                                pageSize,
                                (long) pageNumber * pageSize < count,
                                pageNumber != 1
                        )
                )
                .doOnNext(it -> log.info("paging dto: {}", it));

        Rendering r = Rendering.view("main")
                .modelAttribute("items", partitionItemDto)
                .modelAttribute("paging", pagingDto)
                .modelAttribute("search", search)
                .modelAttribute("sort", sort.name())
                .modelAttribute("authenticated", user != null)
                .build();
        return Mono.just(r);
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@AuthenticationPrincipal User user,
                                                 @PathVariable long id,
                                                 @ModelAttribute ActionDto action,
                                                 @ModelAttribute ViewParamDto viewParamDto) {
        log.info("incoming request for change item quantity in cart from user {}. item id {}, action {}, viewParamDto {}",
                user, id, action, viewParamDto);

        return orderService.changeItemQuantityInCart(user.getId(), id, actionMapper.to(action))
                .flatMap(order ->
                        Mono.just("redirect:/main/items" +
                                "?" +
                                "search=" + viewParamDto.search + "&" +
                                "sort=" + viewParamDto.sort + "&" +
                                "pageSize=" + viewParamDto.pageSize + "&" +
                                "pageNumber=" + viewParamDto.pageNumber));

    }
}
