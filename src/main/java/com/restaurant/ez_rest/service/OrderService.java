package com.restaurant.ez_rest.service;


import com.restaurant.ez_rest.dto.orderDTOs.OrderItemRequestDTO;
import com.restaurant.ez_rest.dto.orderDTOs.OrderRequestDTO;
import com.restaurant.ez_rest.dto.orderDTOs.StatementeResponseDTO;
import com.restaurant.ez_rest.exception.BusinessLogicException;
import com.restaurant.ez_rest.exception.ConflictNameException;
import com.restaurant.ez_rest.exception.ProductNotFoundException;
import com.restaurant.ez_rest.exception.TableNotFoundException;
import com.restaurant.ez_rest.model.*;
import com.restaurant.ez_rest.repository.OrderItemRepository;
import com.restaurant.ez_rest.repository.OrderRepository;
import com.restaurant.ez_rest.repository.ProductRepository;
import com.restaurant.ez_rest.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;


    // 1. Abrir uma Nova Comanda
    @Transactional
    public void openOrder(long tableId) {

        // REGRA 1: Verifica se a Mesa existe
        RestaurantTable restaurantTable = tableRepository.findById(tableId).orElseThrow(() ->  new TableNotFoundException("Mesa com ID: "+tableId
        +", não existe."));

        // REGRA 2: Verifica se já existe uma Comanda ABERTA para essa Mesa
        if (orderRepository.findByTableIdAndOrderStatus(tableId, OrderStatus.OPEN).isPresent()) {
            throw new ConflictNameException("A Mesa " + tableId + " já possui uma COMANDA ABERTA");
        }

        // REGRA 3: Cria a nova Comanda (Order)

        Order nOrder = new Order(restaurantTable);

        // REGRA 4: Persiste a Comanda
        orderRepository.save(nOrder);

        // REGRA 5: Atualiza o Status da Mesa para OCUPADA
        restaurantTable.setTableStatus(TableStatus.OCCUPIED);
        tableRepository.save(restaurantTable);
    }

    // 2. Adicionar Itens à Comanda (Próximo Passo)
    @Transactional
    public void addItemsToOrder(OrderRequestDTO orderRequestDTO) {

        Long tableId = orderRequestDTO.tableId();

        // 1. Validar e buscar a Comanda ATIVA
        Order activeOrder = orderRepository.findByTableIdAndOrderStatus(tableId, OrderStatus.OPEN)
                .orElseThrow(() -> new BusinessLogicException("Não existe Comanda Aberta para a mesa "+tableId+"."));

        // 2. Coletar IDs dos produtos do pedido
        List<Long> productIds = orderRequestDTO.items().stream().map(OrderItemRequestDTO::productId).toList();

        // 3. Buscar todos os Produtos de uma vez
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("Um ou mais produtos do pedido não foram encontrados.");
        }

        // 4. Mapear Produtos para fácil acesso (ID -> Produto)
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));

        BigDecimal totalItemsValue = BigDecimal.ZERO;
        List<OrderItem> newOrderItems = new ArrayList<>();

        // 5. Processar cada Item do Pedido e Construir OrderItems
        for (OrderItemRequestDTO itemDTO : orderRequestDTO.items()) {
            Long productId = itemDTO.productId();
            Product product = productMap.get(productId); // Produto buscado

            // Obter o preço ATUAL do produto para fixar no item do pedido
            BigDecimal unitPrice = product.getPrice();
            Integer quantity = itemDTO.quantity();

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            // Cria o OrderItem
            OrderItem nItem = new OrderItem();
            nItem.setOrder(activeOrder);
            nItem.setProduct(product);
            nItem.setQuantity(quantity);
            nItem.setUnitPrice(unitPrice);
            nItem.setSubtotal(subtotal);

            // Adiciona o novo item à lista da Comanda para salvar
            newOrderItems.add(nItem);

            // Acumula o valor total dos novos itens
            totalItemsValue = totalItemsValue.add(subtotal);
        }

        // 6. Persistir os novos OrderItems
        orderItemRepository.saveAll(newOrderItems);

        // 7. Atualizar o Valor Total da Comanda
        // Soma o valor total antigo + o valor dos novos itens
        BigDecimal newTotal = activeOrder.getTotalValue().add(totalItemsValue);
        activeOrder.setTotalValue(newTotal);

        //Salva a Comanda ATUALIZADA
        orderRepository.save(activeOrder);
    }

    public StatementeResponseDTO getOrderStatment(Long tableId) {
        // 1. Busca a Comanda ABERTA para a Mesa (Se for Extrato, só queremos a ativa)
        Order activeOrder = orderRepository.findByTableIdAndOrderStatus(tableId, OrderStatus.OPEN)
                .orElseThrow(() -> new BusinessLogicException("Não há Comanda ABERTA para a Mesa "+tableId+"." ));

        // 2. Mapeia a Comanda (Order) e seus Itens (OrderItems) para o DTO de Resposta
       return StatementeResponseDTO.fromModel(activeOrder);
    }
}
