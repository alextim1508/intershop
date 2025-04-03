package com.alextim.intershop.listener;

import com.alextim.intershop.entity.Order;
import com.alextim.intershop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    private final OrderRepository orderRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        orderRepository.save(new Order());
    }
}