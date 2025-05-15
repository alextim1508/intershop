package com.alextim.intershop.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("orders_items")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class OrderItem {

    @Id
    private Long id;

    @NonNull
    @Column("order_id")
    private Long orderId;

    @NonNull
    @Column("item_id")
    private Long itemId;

    @Column("count")
    private int quantity;

    public OrderItem(@NonNull Long orderId, @NonNull Long itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}