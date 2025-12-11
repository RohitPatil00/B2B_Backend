package com.marketplace.hyderabad.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSeller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private Double price;
    private Integer moq;
    private Integer stock;
}
