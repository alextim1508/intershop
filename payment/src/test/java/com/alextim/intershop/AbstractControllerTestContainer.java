package com.alextim.intershop;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTestContainer {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected DatabaseClient databaseClient;

    protected WebTestClientConfigurer getMockJwt() {
        return SecurityMockServerConfigurers.mockJwt()
                .jwt(jwt -> jwt
                        .claim("resource_access",
                                "{\n" +
                                        "   \"account\": {\n" +
                                        "       \"roles\": [\n" +
                                        "           \"manage-account\",\n" +
                                        "           \"manage-account-links\",\n" +
                                        "           \"view-profile\"\n" +
                                        "       ]\n" +
                                        "   }\n" +
                                        "}")
                );
    }

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    void clearDatabase() {
        databaseClient.sql("DELETE FROM accounts;") .fetch().rowsUpdated()
                .then()
                .block();
    }

    @AfterEach
    public void tearDown() {
        clearDatabase();
    }
}
