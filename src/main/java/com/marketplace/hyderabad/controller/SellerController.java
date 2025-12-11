package com.marketplace.hyderabad.controller;

import com.marketplace.hyderabad.model.Seller;
import com.marketplace.hyderabad.repository.SellerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    private final SellerRepository sellerRepository;

    public SellerController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Seller> register(@RequestBody Seller seller) {
        Seller s = sellerRepository.save(seller);
        return ResponseEntity.ok(s);
    }
}
