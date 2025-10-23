package com.restaurant.ez_rest.dto.productDTOs;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        BigDecimal price
) {
}
