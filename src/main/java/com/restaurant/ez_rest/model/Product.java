package com.restaurant.ez_rest.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@RestaurantTable(name = "product_item")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    public Product(@NotBlank(message = "Nome do produto é obrigatório") @Size(min = 3, max = 100, message = "O nome deve ter entre {min} e {max} caracteres") String name, @NotNull(message = "Preço do produto é obrigatório.") @DecimalMin(value = "0.01", inclusive = true, message = "Preço do produto deve ser positivo e maior que zero ") BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
