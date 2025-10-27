package com.restaurant.ez_rest.repository;

import com.restaurant.ez_rest.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
}
