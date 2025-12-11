package com.marketplace.hyderabad.controller;

import com.marketplace.hyderabad.model.Order;
import com.marketplace.hyderabad.service.OrderService;
import com.marketplace.hyderabad.repository.OrderRepository;
import com.marketplace.hyderabad.service.dto.CartItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam Long userId, @RequestBody List<CartItemDto> items) {
        Order o = orderService.createOrder(userId, items);
        return ResponseEntity.ok(o);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
