package com.marketplace.hyderabad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Instant createdAt;

    @OneToMany(mappedBy = "parentOrder", cascade = CascadeType.ALL)
    private List<SubOrder> subOrders;
}
