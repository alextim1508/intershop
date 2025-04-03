package com.alextim.intershop.entity;

import com.alextim.intershop.utils.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "orderItems")
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT DEFAULT 'CURRENT'")
    private Status status = Status.CURRENT;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private ZonedDateTime created;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime completed;

    public void addItem(Item item) {
        Optional<OrderItem> first = orderItems.stream()
                .filter(orderItem -> orderItem.getItem().equals(item)).findFirst();

        if(first.isPresent()) {
            OrderItem orderItem = first.get();
            orderItem.setCount(orderItem.getCount() + 1);
        } else {
            orderItems.add(new OrderItem(this, item));
        }
    }
}
