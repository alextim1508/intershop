package com.alextim.intershop.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@ToString
public class ViewParamDto {
    public String sort;
    public String search;
    public Integer pageSize;
    public Integer pageNumber;
}
