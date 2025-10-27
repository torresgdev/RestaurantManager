package com.restaurant.ez_rest.repository;

import com.restaurant.ez_rest.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
}
