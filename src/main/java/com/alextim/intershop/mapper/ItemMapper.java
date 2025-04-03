package com.alextim.intershop.mapper;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    public abstract Item toModel(ItemDto itemDto);

    @Mapping(target = "count", expression = "java(item.getOrderItems().getCount())")
    @Mapping(target = "price", expression = "java(item.getOrderItems().getPrice())")
    public abstract ItemDto toDto(Item item);
}
