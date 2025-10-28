package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.orderDTOs.OrderRequestDTO;
import com.restaurant.ez_rest.dto.orderDTOs.StatementeResponseDTO;
import com.restaurant.ez_rest.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "03. Gerenciamento de Comandas", description = "Ciclo de vida (Abrir, adicionar itens e fechar comanda)")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Abre uma comanda", description = "Cria uma nova Order e muda o status da mesa para Ocupada")
    @PostMapping("/open/{tableId}")
    public ResponseEntity<Void> openOrder(@PathVariable Long tableId) {
        orderService.openOrder(tableId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Adiciona Itens", description = "Adiciona itens do cardápio a comanda")
    @PostMapping("/add-items")
    public ResponseEntity<Void>addItensToOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        orderService.addItemsToOrder(orderRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Fecha a comanda", description = "Fecha a comanda em caso de pagamento e deixa a mesa livre")
    @PostMapping("/close/{tableId}")
    public ResponseEntity<StatementeResponseDTO> closeOrder(@PathVariable Long tableId) {
        var content = orderService.closeOrder(tableId);
        return ResponseEntity.ok(content);
    }
}
