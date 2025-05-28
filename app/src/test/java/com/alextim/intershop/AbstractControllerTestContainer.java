package com.alextim.intershop;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static com.alextim.intershop.service.ItemCacheServiceImpl.CacheName.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTestContainer {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    protected DatabaseClient databaseClient;

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    @ServiceConnection
    static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));

    void clearDatabase() {
        databaseClient.sql("DELETE FROM orders_items;") .fetch().rowsUpdated()
                .then(databaseClient.sql("DELETE FROM orders;").fetch().rowsUpdated())
                .then(databaseClient.sql("DELETE FROM items;").fetch().rowsUpdated())
                .then()
                .block();
    }

    void clearCache() {
        redisTemplate.keys(ITEM + ":*").clear();
        redisTemplate.keys(ITEMS_LIST + ":*").clear();
        redisTemplate.keys(ITEMS_COUNT + ":*").clear();
    }

    @AfterEach
    public void tearDown() {
        clearCache();
        clearDatabase();
    }
}
