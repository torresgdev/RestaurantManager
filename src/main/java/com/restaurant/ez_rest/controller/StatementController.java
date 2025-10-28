package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.orderDTOs.StatementeResponseDTO;
import com.restaurant.ez_rest.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Tag(name = "04. Gerencia o Extrato", description = "Ciclo de consumo de determinada mesa dando valores de acordo com a comanda")
public class StatementController {

    private final OrderService orderService;

    @Operation(summary = "Pega o Extrato", description = "Retorna o valor total de consumo da mesa")
    @GetMapping("/{tableId}")
    public ResponseEntity<StatementeResponseDTO> getStatement(@PathVariable Long tableId) {
        var stat = orderService.getOrderStatment(tableId);
        return ResponseEntity.ok(stat);
    }
}
