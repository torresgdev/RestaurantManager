package com.restaurant.ez_rest;



import com.restaurant.ez_rest.dto.productDTOs.ProductRequestDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductResponseDTO;
import com.restaurant.ez_rest.dto.productDTOs.ProductUpdateDTO;
import com.restaurant.ez_rest.exception.ConflictNameException;
import com.restaurant.ez_rest.exception.ProductNotFoundException;
import com.restaurant.ez_rest.model.Product;
import com.restaurant.ez_rest.repository.ProductRepository;
import com.restaurant.ez_rest.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequestDTO pDTO;
    private ProductResponseDTO response;

    @BeforeEach
    void setUp() {
        pDTO = new ProductRequestDTO("Batata-Frita M", new BigDecimal("18.00"));
    }


    @Test
    @DisplayName("Deve criar com sucesso novo produto")
    void shouldCreateNewProduct() {
        when(productRepository.existsByName(pDTO.name())).thenReturn(false);

        productService.createProduct(pDTO);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar exception para nomes iguais")
    void shouldThrowExceptionConflict() {

        when(productRepository.existsByName(pDTO.name())).thenReturn(true);

        ConflictNameException ex = assertThrows(ConflictNameException.class, () -> {
            productService.createProduct(pDTO);
        }, "Deveria Lançar uma CONFLICT Excepption");

        String message = "Nome de produto já cadastrado em nosso banco, por favor insira outro nome.";

        verify(productRepository, never()).save(any(Product.class));
        assertEquals(message, ex.getMessage(), "Mensagem deve ser igual");
    }

    @Test
    @DisplayName("Deve Listar Produtos Cadastrados")
    void shouldListProductsRegistered() {
        Product p1 = new Product("Cerveja", new BigDecimal("6.00"));
        Product p2 = new Product("Petisco", new BigDecimal("18.00"));
        List<Product> registered = List.of(p1, p2);

        when(productRepository.findAll()).thenReturn(registered);

        List<ProductResponseDTO> listed = productService.listAllProducts();

        verify(productRepository, times(1)).findAll();
        assertNotNull(listed, "A lista nao pode ser nula");
        assertEquals(p1.getId(),listed.getFirst().id(), "O id deve ser correspondente");
        assertEquals(p1.getName(), listed.getFirst().name(), "O nome deve ser correspondete");
    }

    @Test
    @DisplayName("deve retornar um produto pelo id")
    void shouldListProductById() {
        Product product = new Product("Cerveja", new BigDecimal("6.00"));
        Long productId = 1L;
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponseDTO responseDTO = productService.listById(productId);

        verify(productRepository, times(1)).findById(productId);
        assertEquals(productId, responseDTO.id(), "Id do produto deve ser correspondete");
    }

    @Test
    @DisplayName("Deve lançar Excecao de notfound")
    void shouldThrowExceptionNotFound() {
        Long productId = 99L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> {
            productService.listById(productId);
        }, "Deveria Lançar uma Exceção");

        String message = "Produto com esse ID: 99, não foi encontrado";

        assertEquals(message, ex.getMessage(), "A mensagem deve ser precisa");
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Deve atulizar o produto pelo ID")
    void shouldUpdateProduct() {

        Product oldProduct = new Product("Chopp", new BigDecimal("12.00"));
        Long oldProductId = 1L;
        oldProduct.setId(oldProductId);

        ProductUpdateDTO updateDTO = new ProductUpdateDTO(Optional.of("Choppada"), Optional.of(BigDecimal.valueOf(15.00)));

        when(productRepository.findById(oldProductId)).thenReturn(Optional.of(oldProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.updateProduct(oldProductId, updateDTO);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).save(any(Product.class));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product productSaved = productCaptor.getValue();

        assertEquals(oldProductId, productSaved.getId(), "O Id deve ser mantido, garantindo que o objeto correto foi atualizado");
        assertEquals(updateDTO.name().get(), productSaved.getName(), " O nome deve ser correspondente");
        assertEquals(updateDTO.price().get(), productSaved.getPrice(), "O preço deve ser correspondente");
    }

    @Test
    @DisplayName("Deve lançar execção de ProductNotFound")
    void shouldThrowExceptionProductNotFoundWhenTryUpdate() {
        Long productId = 99L;
        ProductUpdateDTO updateDTO = new ProductUpdateDTO(Optional.of("Choppada"), Optional.of(BigDecimal.valueOf(15.00)));


        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, updateDTO);
        }, "Deve lança exception ProductNotFound");

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));

        String message = "Produto com esse ID: "+productId+", não foi encontrado";

        assertEquals(message, exception.getMessage(),"Mensagem deve ser identica");
    }

    @Test
    @DisplayName("Deve Lançar ConflictName se tentar atualizar para um nome existente")
    void shouldThrowConflictExceptionIfNameAlreadyExists() {
        Long id = 1L;
        Product oldProduct = new Product(id, "Cerveja Pilsen", new BigDecimal("14.00")); // O produto original
        ProductUpdateDTO updateDTO = new ProductUpdateDTO(Optional.of("Cerveja"), Optional.of(BigDecimal.valueOf(15.00)));

        when(productRepository.findById(id)).thenReturn(Optional.of(oldProduct));
        when(productRepository.existsByName(updateDTO.name().get())).thenReturn(true);

        ConflictNameException exception = assertThrows(ConflictNameException.class, () -> {
            productService.updateProduct(id, updateDTO);
        }, "Deve Lançar Exception de Conflito");

        String message = "Novo nome de produto já cadastrado.";

        assertEquals(message, exception.getMessage(), "Mensagem devem ser identicas");

        // VERIFICAÇÕES
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).existsByName(updateDTO.name().get());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve Deletar um produto")
    void shouldDeleteProductById() {
        Long pId = 1L;
        Product newProd = new Product("Petisco", new BigDecimal("7.00"));
        newProd.setId(pId);

        when(productRepository.findById(pId)).thenReturn(Optional.of(newProd));

        productService.deleteProduct(pId);

        verify(productRepository, times(1)).findById(pId);
        verify(productRepository, times(1)).deleteById(pId);
    }


}
