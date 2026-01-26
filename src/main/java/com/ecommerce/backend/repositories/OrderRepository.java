package com.ecommerce.backend.repositories;

import com.ecommerce.backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
