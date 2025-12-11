package com.marketplace.hyderabad.service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long productId;
    private Long sellerId;
    private Integer quantity;
}
