package com.marketplace.hyderabad.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "sub_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    private String status; // use enum in service

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "parent_order_id")
    private Order parentOrder;

    @OneToMany(mappedBy = "subOrder", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
