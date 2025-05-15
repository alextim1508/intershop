package com.alextim.intershop.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"description", "imgPath"})
public class ItemDto {
    public long id;
    public String title;
    public String description;
    public String imgPath;
    public int count;
    public double price;
}
