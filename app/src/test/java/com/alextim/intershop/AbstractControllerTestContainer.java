package com.alextim.intershop;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.entity.User;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.repository.OrderItemRepository;
import com.alextim.intershop.repository.OrderRepository;
import com.alextim.intershop.repository.UserRepository;
import com.alextim.intershop.service.PaymentService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static com.alextim.intershop.service.ItemCacheServiceImpl.CacheName.*;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, value = PaymentService.class)

)
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


    static final RedisContainer redisContainer1 = new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));
    static final RedisContainer redisContainer2 = new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));

    static {
        redisContainer1.start();
        redisContainer2.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.cache.host", () -> redisContainer1.getHost());
        registry.add("spring.redis.redis.cache.port", () -> redisContainer1.getFirstMappedPort());
        registry.add("spring.redis.session.host", () -> redisContainer2.getHost());
        registry.add("spring.redis.session.port", () -> redisContainer2.getFirstMappedPort());
    }

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    protected Item item1, item2, item3;
    protected Order order;
    protected User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("username", "pass")).block();

        item1 = itemRepository.save(new Item("item1", "description", "img", 12.0))
                .block();

        item2 = itemRepository.save(new Item("item2", "description", "img", 13.0))
                .block();

        item3 = itemRepository.save(new Item("thing", "description", "img", 14.0))
                .block();

        order = orderRepository.save(new Order(user.getId())).block();

        orderItemRepository.save(new OrderItem(order.getId(), item1.getId(), 2)).block();
        orderItemRepository.save(new OrderItem(order.getId(), item2.getId(), 3)).block();

        ReactiveSecurityContextHolder.clearContext();
    }

    void clearDatabase() {
        databaseClient.sql("DELETE FROM orders_items;").fetch().rowsUpdated()
                .then(databaseClient.sql("DELETE FROM orders;").fetch().rowsUpdated())
                .then(databaseClient.sql("DELETE FROM items;").fetch().rowsUpdated())
                .then(databaseClient.sql("DELETE FROM user_details;").fetch().rowsUpdated())
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
