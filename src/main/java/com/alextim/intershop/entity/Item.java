package com.alextim.intershop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "orderItems")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Item(String title, String description, String imgPath, double price) {
        this.title = title;
        this.description = description;
        this.imgPath = imgPath;
        this.price = price;
    }
}
