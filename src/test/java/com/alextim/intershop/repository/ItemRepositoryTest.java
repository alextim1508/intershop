package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findById_shouldFindItemByIdTest() {
        Item savedItem = itemRepository.save(new Item("title", "description", "url", 1.2));

        Optional<Item> itemById = itemRepository.findById(savedItem.getId());
        Assertions.assertTrue(itemById.isPresent());
        Assertions.assertEquals(savedItem, itemById.get());
    }

    @Test
    public void search_shouldFindByTitleOrDescriptionItemTest() {
        itemRepository.save(new Item("abc", "description", "url", 1.4));
        itemRepository.save(new Item("title", "bcde", "url", 1.2));

        List<Item> items = itemRepository.search("bc", PageRequest.of(0, 5));

        Assertions.assertEquals(2, items.size());
    }

    @Test
    public void findAll_shouldFindSortedByPriceAscItemTest() {
        itemRepository.save(new Item("title", "description", "url", 1.2));
        itemRepository.save(new Item("title", "description", "url", 1.4));
        itemRepository.save(new Item("title", "description", "url", 1.3));

        List<Double> prices = itemRepository.findAll(PageRequest.of(0, 5, Sort.by("price").ascending()))
                .stream().map(Item::getPrice).toList();

        Assertions.assertEquals(List.of(1.2, 1.3, 1.4), prices);
    }

    @Test
    public void findAll_shouldFindSortedByTitleAscItemTest() {
        itemRepository.save(new Item("aa", "description", "url", 1.2));
        itemRepository.save(new Item("cc", "description", "url", 1.4));
        itemRepository.save(new Item("bb", "description", "url", 1.3));

        List<String> titles = itemRepository.findAll(PageRequest.of(0, 5, Sort.by("title").ascending()))
                .stream().map(Item::getTitle).toList();

        Assertions.assertEquals(List.of("aa", "bb", "cc"), titles);
    }

    @Test
    public void findAll_shouldFindItemPageTest() {
        for (int i = 0; i < 10; i++) {
            itemRepository.save(new Item("title" + (i + 1), "description", "url", 1.2));
        }

        List<String> titles = itemRepository.findAll(PageRequest.of(1, 2))
                .stream().map(Item::getTitle).toList();

        Assertions.assertEquals(List.of("title3", "title4"), titles);
    }
}
