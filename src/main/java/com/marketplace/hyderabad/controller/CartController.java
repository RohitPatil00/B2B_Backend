package com.marketplace.hyderabad.controller;

import com.marketplace.hyderabad.service.dto.CartItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody List<CartItemDto> items) {
        // validate stock and seller mapping (stub)
        return ResponseEntity.ok(items);
    }

    @PostMapping("/summary")
    public ResponseEntity<?> summary(@RequestBody List<CartItemDto> items) {
        // compute summary (stub)
        return ResponseEntity.ok(items);
    }
}
