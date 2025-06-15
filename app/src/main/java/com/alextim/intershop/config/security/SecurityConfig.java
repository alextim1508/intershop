package com.alextim.intershop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

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
                                            exchange.getExchange().getResponse()
                                                    .setStatusCode(HttpStatus.OK);
                                        }))
                        )
                )
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(
                                        "/",
                                        "/main/**",
                                        "/items/**")
                                .permitAll()
                                .pathMatchers(
                                        "/cart/**",
                                        "/orders/**",
                                        "/buy/**")
                                .authenticated()
                                .anyExchange().authenticated()
                )
                .csrf(csrf -> csrf.csrfTokenRequestHandler(csrfHandler))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}