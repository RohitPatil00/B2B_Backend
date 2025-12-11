package com.marketplace.hyderabad.service;

import com.marketplace.hyderabad.service.dto.CartItemDto;
import com.marketplace.hyderabad.model.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, List<CartItemDto> items);
}
