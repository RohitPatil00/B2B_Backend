package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
