package com.restaurant.ez_rest.dto.tableDTOs;

import com.restaurant.ez_rest.model.Table;
import com.restaurant.ez_rest.model.TableStatus;

public record TableResponseDTO(
        Long id,
        TableStatus tableStatus,
        String qrCodeUrl
) {
    public static TableResponseDTO fromModel(Table table) {
        return new TableResponseDTO(
                table.getId(),
                table.getTableStatus(),
                table.getQrCodeUrl()
        );

    }
}
