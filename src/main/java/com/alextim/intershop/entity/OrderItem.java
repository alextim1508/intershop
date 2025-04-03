package com.alextim.intershop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Table( name = "orders_items",
        indexes = @Index(name = "uq_items_orders_item_id_order_id", columnList = "order, item", unique = true)
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    private Order order;

    @NonNull
    @ManyToOne
    private Item item;

    @Column(columnDefinition = "INT DEFAULT '0'")
    private int count = 0;
}
