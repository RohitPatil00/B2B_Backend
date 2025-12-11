package com.marketplace.hyderabad.service.impl;

import com.marketplace.hyderabad.model.*;
import com.marketplace.hyderabad.repository.*;
import com.marketplace.hyderabad.service.OrderService;
import com.marketplace.hyderabad.service.dto.CartItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SubOrderRepository subOrderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            SubOrderRepository subOrderRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.subOrderRepository = subOrderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, List<CartItemDto> items) {
        Order parent = Order.builder()
                .userId(userId)
                .createdAt(Instant.now())
                .build();
        parent = orderRepository.save(parent);

        // Group items by sellerId
        Map<Long, List<CartItemDto>> bySeller = items.stream()
                .collect(Collectors.groupingBy(CartItemDto::getSellerId));

        List<SubOrder> subOrders = new ArrayList<>();

        for (Map.Entry<Long, List<CartItemDto>> entry : bySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItemDto> sellerItems = entry.getValue();

            SubOrder sub = SubOrder.builder()
                    .sellerId(sellerId)
                    .status("PENDING")
                    .createdAt(Instant.now())
                    .parentOrder(parent)
                    .build();
            sub = subOrderRepository.save(sub);

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItemDto ci : sellerItems) {
                OrderItem oi = OrderItem.builder()
                        .productId(ci.getProductId())
                        .quantity(ci.getQuantity())
                        .price(0.0) // pricing to be filled from product_seller
                        .subOrder(sub)
                        .build();
                orderItems.add(orderItemRepository.save(oi));
            }
            sub.setItems(orderItems);
            subOrders.add(sub);
        }

        parent.setSubOrders(subOrders);
        return orderRepository.save(parent);
    }
}
