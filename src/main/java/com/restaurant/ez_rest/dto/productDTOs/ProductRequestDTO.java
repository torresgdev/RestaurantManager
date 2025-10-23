package com.restaurant.ez_rest.dto.productDTOs;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Nome do produto é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre {min} e {max} caracteres")
        String name,


        @NotNull(message = "Preço do produto é obrigatório.")
        @DecimalMin(value = "0.01", inclusive = true, message = "Preço do produto deve ser positivo e maior que zero ")
        BigDecimal price
) {
}
