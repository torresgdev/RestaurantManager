package com.restaurant.ez_rest.dto.orderDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
        @NotNull(message = "O ID do produto é obrigatório.")
        Long productId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A Quantidade deve ser pelo menos 1 item")
        Integer quantity
) {
}
