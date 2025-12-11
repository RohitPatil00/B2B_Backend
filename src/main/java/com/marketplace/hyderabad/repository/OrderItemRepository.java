package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
