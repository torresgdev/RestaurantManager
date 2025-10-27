package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.orderDTOs.StatementeResponseDTO;
import com.restaurant.ez_rest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController {

    private final OrderService orderService;

    @GetMapping("/{tableId}")
    public ResponseEntity<StatementeResponseDTO> getStatement(@PathVariable Long tableId) {
        var stat = orderService.getOrderStatment(tableId);
        return ResponseEntity.ok(stat);
    }
}
