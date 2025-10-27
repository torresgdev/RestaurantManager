package com.restaurant.ez_rest.repository;

import com.restaurant.ez_rest.model.Order;
import com.restaurant.ez_rest.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByTableIdAndOrderStatus(Long tableId, OrderStatus status);
}
