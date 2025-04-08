package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.entity.OrderItem;
import com.alextim.intershop.exeption.ItemNotFoundException;
import com.alextim.intershop.repository.ItemRepository;
import com.alextim.intershop.utils.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.alextim.intershop.utils.Status.CURRENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        log.info("saving item: {}", item);
        return itemRepository.save(item);
    }

    @Override
    public Entry<Item, Integer> findById(long id) {
        log.info("find item by id {}", id);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        log.info("item: {}", item);

        Integer count = item.getOrderItems()
                .stream()
                .filter(it -> it.getOrder().getStatus().equals(CURRENT))
                .map(OrderItem::getCount)
                .findFirst()
                .orElse(0);
        log.info("count: {}", count);

        return new SimpleEntry<>(item, count);
    }

    @Override
    public Map<Item, Integer> search(String search, SortType sort, int pageNumber, int pageSize) {
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

        Map<Item, Integer> itemCounts = items.stream()
                .collect(Collectors.toMap(
                        item -> item,
                        item -> item.getOrderItems().stream()
                                .filter(it -> it.getOrder().getStatus().equals(CURRENT))
                                .map(OrderItem::getCount)
                                .findFirst()
                                .orElse(0),
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));
        log.info("item counts: {}", itemCounts);

        return itemCounts;
    }

    @Override
    public long count(String search) {
        log.info("count of items by search string {}", search);

        long count;
        if (search.isEmpty()) {
            count = itemRepository.count();
        } else {
            count = itemRepository.countBySearch(search);
        }

        log.info("item count: {}", count);

        return count;
    }
}
