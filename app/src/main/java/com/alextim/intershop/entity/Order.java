package com.alextim.intershop.entity;

import com.alextim.intershop.utils.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;


@Table(name = "orders")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Order {

    @Id
    private Long id;

    @NonNull
    @Column("user_id")
    private Long userId;

    @NonNull
    private Status status = Status.CURRENT;

    @NonNull
    private ZonedDateTime created = ZonedDateTime.now();

    private ZonedDateTime completed;

    public Order(long userId) {
        this.userId = userId;
    }
}