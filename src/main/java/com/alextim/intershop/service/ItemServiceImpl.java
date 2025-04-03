package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.utils.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item findById(long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        log.info("item by id {}: {}", id, item);

        return item;
    }

    @Override
    public List<Item> search(String search, SortType sort, int pageNumber, int pageSize) {
        log.info("search {}, sort {}, pageNumber {}, pageSize {}", search, sort, pageNumber, pageSize);

        PageRequest pageRequest = switch (sort) {
            case NO -> PageRequest.of(pageNumber, pageSize);
            case ALPHA -> PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            case PRICE -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
        };
        log.info("pageRequest: {}", pageRequest);

        List<Item> items;
        if (search.isEmpty()) {
            items = itemRepository.findAll(pageRequest).getContent();
        } else {
            items = itemRepository.search(search, pageRequest);
        }
        log.info("items: {}", items);

        return items;
    }

    @Override
    public long count() {
        long count = itemRepository.count();
        log.info("item count: {}", count);
        return count;
    }
}
