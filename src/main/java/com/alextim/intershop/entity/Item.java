package com.alextim.intershop.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "items")
@Getter
@Setter
@ToString(exclude = { "description", "imgPath"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
public class Item {

    @Id
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    @Column("img_path")
    private String imgPath;

    @NonNull
    private Double price;
}