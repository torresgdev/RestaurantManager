package com.restaurant.ez_rest.controller;


import com.restaurant.ez_rest.dto.productDTOs.ProductRequestDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductResponseDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductUpdateDTO;
import com.restaurant.ez_rest.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts() {
        var content = productService.listAllProducts();
        return ResponseEntity.ok(content);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> listProductsById(@PathVariable Long id) {
        var content = productService.listById(id);
        return ResponseEntity.ok(content);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@Valid @PathVariable Long id,
                                                            @RequestBody ProductUpdateDTO updateDTO) {
        var content = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
