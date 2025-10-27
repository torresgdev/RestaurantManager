package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.tableDTOs.TableRequestDTO;
import com.restaurant.ez_rest.dto.tableDTOs.TableResponseDTO;
import com.restaurant.ez_rest.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;


    @PostMapping
    public ResponseEntity<TableResponseDTO> createTable(@RequestBody TableRequestDTO requestDTO) {
        var content = tableService.createTable(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @GetMapping
    public ResponseEntity<List<TableResponseDTO>> listAllTables() {
        var content = tableService.listAllTables();
        return ResponseEntity.ok(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableResponseDTO> listById(@PathVariable Long id) {
        var content = tableService.listById(id);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
