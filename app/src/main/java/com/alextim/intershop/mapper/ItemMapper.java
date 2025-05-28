package com.alextim.intershop.mapper;

import com.alextim.intershop.dto.ItemDto;
import com.alextim.intershop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Mapping(target = "count", source = "quantity")
    public abstract ItemDto toDto(Item item, int quantity);
}
