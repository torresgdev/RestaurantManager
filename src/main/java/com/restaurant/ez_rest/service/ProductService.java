package com.restaurant.ez_rest.service;


import com.restaurant.ez_rest.dto.productDTOs.ProductRequestDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductResponseDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductUpdateDTO;
import com.restaurant.ez_rest.exception.ConflictNameException;
import com.restaurant.ez_rest.exception.ProductNotFoundException;
import com.restaurant.ez_rest.model.Product;
import com.restaurant.ez_rest.repository.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    // CRIA O PRODUTO NO BANCO DE DADOS
    public void createProduct(ProductRequestDTO requestDTO) {

        if (productRepository.existsByName(requestDTO.name())) {
            throw new ConflictNameException("Nome de produto já cadastrado em nosso banco, por favor insira outro nome.");
        }

        Product nProduct = new Product(requestDTO.name(),requestDTO.price());

        productRepository.save(nProduct);
    }

    // LISTA TODOS OS PRODUTOS CADASTRADOS
    public List<ProductResponseDTO> listAllProducts() {
        return productRepository.findAll().stream().map(ProductResponseDTO::fromModel).toList();
    }

    //LISTA UM PRODUTO PELO ID
    public ProductResponseDTO listById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Produto com " +
                "esse ID: "+id+", não foi encontrado"));

        return ProductResponseDTO.fromModel(product);
    }

    // ATUALIZA UM PRODUTO PELO ID
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updateDTO) {
        Product toUpdate = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Produto com " +
                "esse ID: "+id+", não foi encontrado"));

        // verificação de nome ja existente no banco ou em branco
        updateDTO.name().ifPresent(newName -> {
            if (!newName.equals(toUpdate.getName()) && productRepository.existsByName(newName)) {
                throw new ConflictNameException("Novo nome de produto já cadastrado.");
            }
            toUpdate.setName(newName);
        });

        //Atualiza o PREÇO se presente no DTO
        updateDTO.price().ifPresent(toUpdate::setPrice);

        Product savedProduct = productRepository.save(toUpdate);

        return ProductResponseDTO.fromModel(savedProduct);
    }

    //DELETA UM PRODUTO
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Produto com " +
                "esse ID: "+id+", não foi encontrado"));
        productRepository.deleteById(id);
    }



}
