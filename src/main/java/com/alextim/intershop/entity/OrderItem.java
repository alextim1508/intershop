package com.alextim.intershop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "orders_items",
        indexes = @Index(name = "uq_items_orders_item_id_order_id", columnList = "order, item", unique = true))
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Item item;

    @Column(columnDefinition = "INT DEFAULT '0'")
    private int count = 0;

    public OrderItem(@NonNull Order order, @NonNull Item item) {
        this.order = order;
        this.item = item;
        this.count = 1;
    }
}
