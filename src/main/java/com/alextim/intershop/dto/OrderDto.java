package com.alextim.intershop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OrderDto {
    public long id;
    public List<ItemDto> items;
    public double totalSum;
}
