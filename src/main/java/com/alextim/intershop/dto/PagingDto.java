package com.alextim.intershop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PagingDto {
    public int pageNumber;
    public int pageSize;
    public boolean hasNext;
    public boolean hasPrevious;
}
