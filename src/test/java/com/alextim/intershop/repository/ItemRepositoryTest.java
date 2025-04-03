package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.Order;
import com.alextim.intershop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void createItemTest() {
        Item savedItem = itemRepository.save(new Item("title", "description", "url", 1.2));

        entityManager.clear();

        Optional<Item> itemById = itemRepository.findById(savedItem.getId());
        Assertions.assertTrue(itemById.isPresent());
        Assertions.assertEquals(savedItem, itemById.get());
    }

}
