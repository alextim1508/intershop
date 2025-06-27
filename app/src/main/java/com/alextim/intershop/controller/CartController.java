package com.alextim.intershop.controller;

import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.dto.ActionDto;
import com.alextim.intershop.dto.CartDto;
import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.mapper.ActionMapper;
import com.alextim.intershop.mapper.ItemMapper;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@Controller
@RequestMapping(value = "/cart/items")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;

    @GetMapping
    public Mono<Rendering> getCart(@AuthenticationPrincipal User user) {
        log.info("incoming request for getting current order from user {}", user);

        return orderService.findCurrentOrder(user.getId())
                .flatMap(order -> getCartDtoMono( user.getId(), order))
                .doOnNext(cartDto -> log.info("cartDto: {}", cartDto))
                .map(cartDto -> Rendering.view("cart")
                        .modelAttribute("items", cartDto.items)
                        .modelAttribute("total", cartDto.total)
                        .modelAttribute("empty", cartDto.empty)
                        .modelAttribute("availablePayment", cartDto.isAvailablePayment())
                        .modelAttribute("possibleToBuy", cartDto.isPossibleToBuy())
                        .build()
                );
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@AuthenticationPrincipal User user,
                                                 @PathVariable long id,
                                                 @ModelAttribute ActionDto action) {
        log.info("incoming request for change item quantity in cart from user {}. item id {}, action {}",
                user, id, action);

        return orderService.changeItemQuantityInCart(user.getId(), id, actionMapper.to(action))
                .then(Mono.just("redirect:/cart/items"));
    }

    private Mono<CartDto> getCartDtoMono(long userId, Order order) {
        return Mono.zip(
                        orderService.findItemsWithQuantityByOrderId(userId, order.getId())
                                .collectMap(Entry::getKey, Entry::getValue),
                        paymentService.getBalance()
                )
                .map(tuple -> {
                    Map<Item, Integer> map = tuple.getT1();

                    List<ItemDto> itemDtos = new ArrayList<>();
                    double totalSum = 0;
                    for (Entry<Item, Integer> entry : map.entrySet()) {
                        itemDtos.add(itemMapper.toDto(entry.getKey(), entry.getValue()));
                        totalSum += orderService.calcPrice(entry.getKey(), entry.getValue());
                    }
                    log.info("total sum: {} of order {}", totalSum, order);

                    ResponseEntity<BalanceResponse> balanceResponse = tuple.getT2();

                    return new CartDto(itemDtos,
                            totalSum,
                            itemDtos.isEmpty(),
                            balanceResponse.getStatusCode() != HttpStatusCode.valueOf(500),
                            balanceResponse.getBody().getBalance() > totalSum);
                });
    }
}