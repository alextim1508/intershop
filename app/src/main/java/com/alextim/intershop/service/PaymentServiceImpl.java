package com.alextim.intershop.service;

import com.alextim.intershop.client.pay.api.BalanceApi;
import com.alextim.intershop.client.pay.api.PaymentsApi;
import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.client.pay.dto.PaymentRequest;
import com.alextim.intershop.client.pay.dto.PaymentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private BalanceApi balanceApi;
    private PaymentsApi paymentsApi;

    @PostConstruct
    public void init() {
        balanceApi = new BalanceApi();
        paymentsApi = new PaymentsApi();
    }

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalance(long userId) {
        log.info("Get balance for user with ID {}", userId);

        return authorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("intershop")
                        .principal("system")
                        .build())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .flatMap(accessToken -> {
                    balanceApi.getApiClient().setBearerToken(accessToken);
                    return balanceApi.balanceGetWithHttpInfo(userId);
                })
                .doOnSuccess(response ->
                        log.info("Get user with {} balance response {} (code: {})",
                                userId, response.getBody(), response.getStatusCode()));
    }

    @Override
    public Mono<ResponseEntity<PaymentResponse>> payment(long userId, double amount) {
        log.info("Payment for user with ID {} of amount {}", userId, amount);

        return authorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("intershop")
                        .principal("system")
                        .build())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .flatMap(accessToken -> {
                    paymentsApi.getApiClient().setBearerToken(accessToken);
                    return paymentsApi.payPostWithHttpInfo(new PaymentRequest().userId(userId).amount(amount))
                            .doOnNext(response ->
                                    log.info("Get user with {} payment response {} (code: {})",
                                            userId, response.getBody(), response.getStatusCode()));
                });
    }
}
