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
public class CartDto {
    public List<ItemDto> items;
    public double total;
    public boolean empty;
    private boolean availablePayment;
    private boolean possibleToBuy;
}
