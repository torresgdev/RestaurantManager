package com.restaurant.ez_rest.dto.orderDTOs;

import com.restaurant.ez_rest.model.OrderItem;

import java.math.BigDecimal;

public record StatementItemResponseDTO(
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal

) {
    public static StatementItemResponseDTO fromModel(OrderItem item) {
        return new StatementItemResponseDTO(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
