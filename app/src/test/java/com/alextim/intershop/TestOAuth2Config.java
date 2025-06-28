package com.alextim.intershop;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

@TestConfiguration
public class TestOAuth2Config {

    @Bean
    @Primary
    public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager() {
        return Mockito.mock(ReactiveOAuth2AuthorizedClientManager.class);
    }

    @Bean
    @Primary
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository() {
        return Mockito.mock(ReactiveClientRegistrationRepository.class);
    }

    @Bean
    @Primary
    public ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService() {
        return Mockito.mock(ReactiveOAuth2AuthorizedClientService.class);
    }
}
