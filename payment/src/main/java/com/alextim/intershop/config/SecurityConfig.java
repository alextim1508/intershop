package com.alextim.intershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/balance").authenticated()
                                .pathMatchers("/pay").authenticated()
                                .anyExchange().authenticated()
                )
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(jwtSpec -> {
                            var jwtAuthConverter = new ReactiveJwtAuthenticationConverter();
                            jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                                Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
                                List<String> roles = (List<String>) account.get("roles");
                                return Flux.fromIterable(roles)
                                        .map(SimpleGrantedAuthority::new);
                            });
                            jwtSpec.jwtAuthenticationConverter(jwtAuthConverter);
                        })
                )
                .build();
    }
}
