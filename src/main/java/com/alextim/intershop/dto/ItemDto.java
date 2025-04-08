package com.alextim.intershop.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"description", "imgPath"})
public class ItemDto {
    public long id;
    public String title;
    public String description;
    public String imgPath;
    public int count;
    public int price;
}
