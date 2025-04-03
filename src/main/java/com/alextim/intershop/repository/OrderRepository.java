package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Status status);
}
