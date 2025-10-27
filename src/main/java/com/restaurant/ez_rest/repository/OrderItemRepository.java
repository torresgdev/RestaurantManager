package com.restaurant.ez_rest.repository;

import com.restaurant.ez_rest.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
