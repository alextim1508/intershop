package com.alextim.intershop.service;

import com.alextim.intershop.entity.Item;
import com.alextim.intershop.utils.SortType;

import java.util.List;

public interface ItemService {

    Item findById(long id);

    List<Item> search(String search, SortType sort, int pageNumber, int pageSize);

    long count();
}
