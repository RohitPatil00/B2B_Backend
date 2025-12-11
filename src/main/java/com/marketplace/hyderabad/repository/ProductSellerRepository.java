package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.ProductSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductSellerRepository extends JpaRepository<ProductSeller, Long> {
    List<ProductSeller> findByProductId(Long productId);
    List<ProductSeller> findBySellerId(Long sellerId);
}
