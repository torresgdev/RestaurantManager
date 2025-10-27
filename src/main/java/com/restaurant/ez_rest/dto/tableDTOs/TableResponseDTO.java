package com.restaurant.ez_rest.dto.tableDTOs;

import com.restaurant.ez_rest.model.RestaurantTable;
import com.restaurant.ez_rest.model.TableStatus;

public record TableResponseDTO(
        Long id,
        TableStatus tableStatus,
        String qrCodeUrl
) {
    public static TableResponseDTO fromModel(RestaurantTable restaurantTable) {
        return new TableResponseDTO(
                restaurantTable.getId(),
                restaurantTable.getTableStatus(),
                restaurantTable.getQrCodeUrl()
        );

    }
}
