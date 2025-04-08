package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.utils.SortType;

import java.util.Map;

public interface ItemService {

    Item save(Item item);

    Map.Entry<Item, Integer> findById(long id);

    Map<Item, Integer> search(String search, SortType sort, int pageNumber, int pageSize);

    long count(String search);
}
