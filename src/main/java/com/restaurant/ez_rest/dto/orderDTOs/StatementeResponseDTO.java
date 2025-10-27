package com.restaurant.ez_rest.dto.orderDTOs;

import com.restaurant.ez_rest.model.Order;
import com.restaurant.ez_rest.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StatementeResponseDTO(
        Long orderId,
        Long tableId,
        OrderStatus status,
        LocalDateTime openedAt,
        List<StatementItemResponseDTO> items,
        BigDecimal totalValue
) {
    public static StatementeResponseDTO fromModel(Order order) {
        // Mapeia a lista de OrderItem para ExtratoItemResponseDTO
        List<StatementItemResponseDTO> itemsDTOs = order.getItems().stream().map(StatementItemResponseDTO::fromModel).toList();

        return new StatementeResponseDTO(
                order.getId(),
                order.getTable().getId(),
                order.getOrderStatus(),
                order.getOpenedAt(),
                itemsDTOs,
                order.getTotalValue()
        );
    }
}
