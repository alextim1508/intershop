package com.alextim.intershop.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableRedisWebSession
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(@Value("${spring.redis.session.host}") String host,
                                                           @Value("${spring.redis.session.port}") Integer port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        var csrfHandler = new XorServerCsrfTokenRequestAttributeHandler();
        csrfHandler.setTokenFromMultipartDataEnabled(true);

        return http
                .formLogin(withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) ->
                                exchange.getExchange().getSession()
                                        .flatMap(WebSession::invalidate)
                                        .then(Mono.fromRunnable(() -> {
                                            ServerHttpResponse response = exchange.getExchange().getResponse();
                                            response.setStatusCode(HttpStatus.FOUND);
                                            response.getHeaders().setLocation(URI.create("/main/items"));
                                        }))
                        )
                )
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/").permitAll()
                                .pathMatchers(HttpMethod.GET, "/main/items").permitAll()
                                .pathMatchers(HttpMethod.GET, "/items/**").permitAll()
                                .pathMatchers("/main/items/**").authenticated()
                                .pathMatchers("/items/**").authenticated()
                                .pathMatchers("/cart/**").authenticated()
                                .pathMatchers("/orders/**").authenticated()
                                .pathMatchers("/buy").authenticated()
                                .anyExchange().authenticated()
                )
                .csrf(csrf -> csrf.csrfTokenRequestHandler(csrfHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        var manager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build()
        );

        return manager;
    }
}