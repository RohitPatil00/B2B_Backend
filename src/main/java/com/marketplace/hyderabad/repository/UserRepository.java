package com.marketplace.hyderabad.repository;

import com.marketplace.hyderabad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phone);
}
