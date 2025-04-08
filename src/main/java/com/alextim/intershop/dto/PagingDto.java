package com.alextim.intershop.dto;

public record PagingDto(int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {
}
