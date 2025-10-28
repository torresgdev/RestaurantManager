package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.productDTOs.ProductRequestDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductResponseDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductUpdateDTO;
import com.restaurant.ez_rest.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "01. Gerenciamento de produtos", description = "Ciclo de vida dos produtos no Cardápio(CRUD COMPLETO)")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Criar produto", description = "Criar produto para o cardápio")
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listar produtos", description = "Listar todos os produtos cadastrados no cardápio")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts() {
        var content = productService.listAllProducts();
        return ResponseEntity.ok(content);
    }

    @Operation(summary = "Listar produto por ID", description = "Listar o produto pelo ID fornecido")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> listProductsById(@PathVariable Long id) {
        var content = productService.listById(id);
        return ResponseEntity.ok(content);
    }

    @Operation(summary = "Atualizar o produto", description = "Atualização de produtos como preço e nome")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @PathVariable Long id,
                                                            @RequestBody ProductUpdateDTO updateDTO) {
        var content = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(content);
    }

    @Operation(summary = "Deletar produto", description = "Deletar produto pelo ID fornecido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
