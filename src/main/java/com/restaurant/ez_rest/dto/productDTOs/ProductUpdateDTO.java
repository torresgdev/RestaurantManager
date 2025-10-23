package com.restaurant.ez_rest.dto.productDTOs;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.Optional;

public record ProductUpdateDTO(
        Optional<String> name,

        @DecimalMin(value = "0.01", inclusive = true, message = "Preço do produto deve ser positivo e maior que zero ")
        Optional<BigDecimal> price
) {
}
