package com.restaurant.ez_rest.dto.orderDTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record OrderRequestDTO(
        @NotNull(message = "O ID da mesa é obrigatório")
        Long tableId,

        @Valid
        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        List<OrderItemRequestDTO> items
) {
}
