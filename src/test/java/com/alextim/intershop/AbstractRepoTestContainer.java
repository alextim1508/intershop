package com.alextim.intershop;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.r2dbc.core.DatabaseClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
public abstract class AbstractRepoTestContainer {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    protected DatabaseClient databaseClient;

    @AfterEach
    public void clearDatabase() {
        databaseClient.sql("DELETE FROM orders_items;") .fetch().rowsUpdated()
                .then(databaseClient.sql("DELETE FROM orders;").fetch().rowsUpdated())
                .then(databaseClient.sql("DELETE FROM items;").fetch().rowsUpdated())
                .then()
                .block();
    }
}
