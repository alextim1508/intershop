package com.alextim.intershop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "items")
@Getter
@Setter
@ToString(exclude = "orderItems")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String title;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false)
    private String imgPath;

    @NonNull
    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();
}
