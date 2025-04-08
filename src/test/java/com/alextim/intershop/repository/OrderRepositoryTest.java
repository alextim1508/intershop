package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.alextim.intershop.utils.Status.COMPLETED;
import static com.alextim.intershop.utils.Status.CURRENT;

@ActiveProfiles("test")
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findByStatus_shouldFindOrderByStatusTest() {
        Order order1 = new Order();
        order1.setStatus(COMPLETED);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setStatus(COMPLETED);
        orderRepository.save(order2);

        orderRepository.save(new Order());

        List<Order> current = orderRepository.findByStatus(CURRENT);
        Assertions.assertEquals(1, current.size());

        List<Order> completed = orderRepository.findByStatus(COMPLETED);
        Assertions.assertEquals(2, completed.size());
    }
}
