package com.restaurant.ez_rest.dto.productDTOs;

import com.restaurant.ez_rest.model.Product;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal price
) {
    public static ProductResponseDTO fromModel(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
