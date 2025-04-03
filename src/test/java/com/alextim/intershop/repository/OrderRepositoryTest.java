package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findByStatus_shouldCreateCurrentOrderWhenAppStartTest() {
        List<Order> current = orderRepository.findByStatus(Status.CURRENT);
        Assertions.assertEquals(1, current.size());
    }
}
