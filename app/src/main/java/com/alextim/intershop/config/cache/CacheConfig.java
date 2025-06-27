package com.alextim.intershop.config.cache;

import com.alextim.intershop.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public ReactiveRedisConnectionFactory cacheRedisConnectionFactory(
            @Value("${spring.redis.cache.host}") String host,
            @Value("${spring.redis.cache.port}") Integer port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveStringRedisTemplate(
            ReactiveRedisConnectionFactory cacheRedisConnectionFactory) {

        return new ReactiveRedisTemplate<>(cacheRedisConnectionFactory,
                RedisSerializationContext.<String, String>newSerializationContext(new StringRedisSerializer())
                        .value(new Jackson2JsonRedisSerializer<>(String.class))
                        .build()
        );
    }

    @Bean
    public ReactiveRedisTemplate<String, Item> reactiveItemRedisTemplate(
            ReactiveRedisConnectionFactory cacheRedisConnectionFactory) {

        return new ReactiveRedisTemplate<>(cacheRedisConnectionFactory,
                RedisSerializationContext.<String, Item>newSerializationContext(new StringRedisSerializer())
                        .value(new Jackson2JsonRedisSerializer<>(Item.class))
                        .build()
        );
    }

    @Bean
    public ReactiveRedisTemplate<String, List<Item>> reactiveItemListRedisTemplate(
            ReactiveRedisConnectionFactory cacheRedisConnectionFactory, ObjectMapper objectMapper) {

        return new ReactiveRedisTemplate<>(cacheRedisConnectionFactory,
                RedisSerializationContext.<String, List<Item>>newSerializationContext(new StringRedisSerializer())
                        .value(new ItemListRedisSerializer(objectMapper))
                        .build()
        );
    }
}