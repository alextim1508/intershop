package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(Status status);

    @Query(value = "SELECT * FROM Orders o where o.status = 'CURRENT' LIMIT 1", nativeQuery = true)
    Optional<Order> findCurrentOrder();
}
