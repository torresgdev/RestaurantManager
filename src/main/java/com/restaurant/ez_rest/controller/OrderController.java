package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.orderDTOs.OrderRequestDTO;
import com.restaurant.ez_rest.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/open/{tableId}")
    public ResponseEntity<Void> openOrder(@PathVariable Long tableId) {
        orderService.openOrder(tableId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add-items")
    public ResponseEntity<Void>addItensToOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        orderService.addItemsToOrder(orderRequestDTO);
        return ResponseEntity.ok().build();
    }
}
