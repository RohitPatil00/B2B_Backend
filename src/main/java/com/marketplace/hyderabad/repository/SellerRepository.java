package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
