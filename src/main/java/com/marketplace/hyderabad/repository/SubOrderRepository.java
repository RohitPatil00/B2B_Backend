package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubOrderRepository extends JpaRepository<SubOrder, Long> {
    List<SubOrder> findByParentOrderId(Long parentOrderId);
    List<SubOrder> findBySellerId(Long sellerId);
}
