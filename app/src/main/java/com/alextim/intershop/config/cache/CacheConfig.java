package com.alextim.intershop.config.cache;

import com.alextim.intershop.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public ReactiveRedisTemplate<String, Item> reactiveItemRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        return new ReactiveRedisTemplate<>(connectionFactory,
                RedisSerializationContext.<String, Item>newSerializationContext(new StringRedisSerializer())
                        .value(new Jackson2JsonRedisSerializer<>(Item.class))
                        .build()
        );
    }

    @Bean
    public ReactiveRedisTemplate<String, List<Item>> reactiveItemListRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {

        return new ReactiveRedisTemplate<>(connectionFactory,
                RedisSerializationContext.<String, List<Item>>newSerializationContext(new StringRedisSerializer())
                        .value(new ItemListRedisSerializer(objectMapper))
                        .build()
        );
    }
}