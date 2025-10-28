package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.tableDTOs.TableRequestDTO;
import com.restaurant.ez_rest.dto.tableDTOs.TableResponseDTO;
import com.restaurant.ez_rest.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@Tag(name = "02. Gerenciamento das Mesas", description = "Ciclo de Vida das mesas disponiveis(Criação,listagem e deleção")
public class TableController {

    private final TableService tableService;


    @Operation(summary = "Criar mesa", description = "Cria uma mesa para clientes")
    @PostMapping
    public ResponseEntity<TableResponseDTO> createTable(@RequestBody TableRequestDTO requestDTO) {
        var content = tableService.createTable(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @Operation(summary = "Lista mesas criadas", description = "Lista mesas criadas e diz se estão ocupadas ou livres")
    @GetMapping
    public ResponseEntity<List<TableResponseDTO>> listAllTables() {
        var content = tableService.listAllTables();
        return ResponseEntity.ok(content);
    }

    @Operation(summary = "Lista mesa por ID", description = "Lista mesas criadas por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TableResponseDTO> listById(@PathVariable Long id) {
        var content = tableService.listById(id);
        return ResponseEntity.ok(content);
    }

    @Operation(summary = "Deleta mesas", description = "Deleta mesas pelo ID fornecido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
