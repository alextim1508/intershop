package com.alextim.intershop.controller;

import com.alextim.intershop.dto.AmountDto;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.service.OrderService;
import com.alextim.intershop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/buy")
@RequiredArgsConstructor
@Slf4j
public class BuyController {

    private final OrderService orderService;

    private final PaymentService paymentService;

    @PostMapping
    public Mono<String> buy(@AuthenticationPrincipal User user,
                            @ModelAttribute AmountDto amountDto) {
        log.info("incoming request from user {} for buying of amount: {}", user, amountDto.amount);

        return paymentService.payment(user.getId(), amountDto.amount)
                .map(response -> response.getBody().getSuccess())
                .flatMap(success ->
                        orderService.completeCurrentOrder(user.getId())
                                .flatMap(completedOrder -> Mono.just("redirect:/orders/" + completedOrder.getId() +
                                        "?" +
                                        "newOrder=" + success + "&" +
                                        "rejectedOrder=" + !success)
                                )
                );
    }
}
