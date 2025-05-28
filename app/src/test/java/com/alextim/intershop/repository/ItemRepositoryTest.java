package com.alextim.intershop.repository;

import com.alextim.intershop.AbstractRepoTestContainer;
import com.alextim.intershop.entity.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class ItemRepositoryTest extends AbstractRepoTestContainer {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void findByTitleOrDescriptionContains_shouldFindItemByTitle() {
        Iterable<Item> items = itemRepository.saveAll(Arrays.asList(
                        new Item("title1", "description1", "img", 0.0),
                        new Item("title2", "description2", "img", 0.0)))
                .thenMany(itemRepository.findByTitleOrDescriptionContains("title1", PageRequest.of(0, 10)))
                .toIterable();

        Assertions.assertThat(items)
                .withFailMessage("Items is empty")
                .isNotEmpty()
                .withFailMessage("Size of items is not 1")
                .hasSize(1)
                .element(0)
                .withFailMessage("Title is unexpected")
                .extracting(Item::getTitle)
                .isEqualTo("title1");
    }

    @Test
    public void findByTitleOrDescriptionContains_shouldFindAllItems() {
        Iterable<Item> items = itemRepository.saveAll(Arrays.asList(
                        new Item("title1", "description1", "img", 0.0),
                        new Item("title2", "description2", "img", 0.0)))
                .thenMany(itemRepository.findAllBy(PageRequest.of(0, 10)))
                .toIterable();

        Assertions.assertThat(items)
                .withFailMessage("Items is empty")
                .isNotEmpty()
                .withFailMessage("Size of items is not 1")
                .hasSize(2)
                .extracting("title")
                .contains("title1", "title2");
    }

    @Test
    public void findByTitleOrDescriptionContains_shouldFindItemWithCommonSubstring() {
        Iterable<Item> items = itemRepository.saveAll(Arrays.asList(
                        new Item("title", "description", "img", 0.0),
                        new Item("super title", "super description", "img", 0.0)))
                .thenMany(itemRepository.findByTitleOrDescriptionContains("super", PageRequest.of(0, 10)))
                .toIterable();

        Assertions.assertThat(items)
                .withFailMessage("Items is empty")
                .isNotEmpty()
                .withFailMessage("Size of items is not 1")
                .hasSize(1)
                .element(0)
                .withFailMessage("Title is unexpected")
                .extracting(Item::getTitle)
                .isEqualTo("super title");
    }

    @Test
    public void countByTitleOrDescriptionContains_shouldReturnItemCountWithCommonSubstring() {
        Long count = itemRepository.saveAll(Arrays.asList(
                        new Item("title", "description", "img", 0.0),
                        new Item("super title", "super description", "img", 0.0)))
                .then(itemRepository.countByTitleOrDescriptionContains("super"))
                .block();

        Assertions.assertThat(count)
                .withFailMessage("Items is empty")
                .isNotNull()
                .withFailMessage("Size of items is not 1")
                .isEqualTo(1);
    }
}
