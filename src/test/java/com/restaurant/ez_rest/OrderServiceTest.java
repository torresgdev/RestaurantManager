package com.restaurant.ez_rest;


import com.restaurant.ez_rest.exception.BusinessLogicException;
import com.restaurant.ez_rest.model.Order;
import com.restaurant.ez_rest.model.OrderStatus;
import com.restaurant.ez_rest.model.RestaurantTable;
import com.restaurant.ez_rest.model.TableStatus;
import com.restaurant.ez_rest.repository.OrderRepository;
import com.restaurant.ez_rest.repository.TableRepository;
import com.restaurant.ez_rest.service.OrderService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private OrderService service;




    @Test
    @Disabled("Deve abrir uma nova comanda e marcar a mesa como OCCUPIED com sucesso")
    void shouldCreateNewOrderSuccess() {
        Long tableId = 5L;

        // 1. SETUP: Criar a Mesa no estado inicial (FREE)
        RestaurantTable freeTable = new RestaurantTable();
        freeTable.setId(tableId);
        freeTable.setTableStatus(TableStatus.FREE);

        // 2. SIMULAÇÃO 1: O Serviço busca e encontra a mesa LIVRE
        when(tableRepository.findById(tableId)).thenReturn(Optional.of(freeTable));

        // 3. SIMULAÇÃO 2: Simula o salvamento da NOVA Comanda.
        // O save deve retornar a instância da Order com um ID.
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order newOrder = invocation.getArgument(0);
            newOrder.setId(10L);
            return newOrder;
        });

        // 4. SIMULAÇÃO 3: Simula o salvamento da Mesa (agora OCCUPIED)
        // Usamos um ArgumentCaptor para verificar se a mesa foi salva com o status CORRETO.
        ArgumentCaptor<RestaurantTable> tableCaptor = ArgumentCaptor.forClass(RestaurantTable.class);
        when(tableRepository.save(tableCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // 5. EXECUÇÃO
        service.openOrder(tableId);

        // 6. VERIFICAÇÃO CRUCIAL: Verificação da Mesa (se o status mudou)
        // Captura o objeto RestaurantTable que foi enviado para ser salvo.
        RestaurantTable savedTable = tableCaptor.getValue();

        assertEquals(TableStatus.OCCUPIED, savedTable.getTableStatus(), "O status da mesa deve ser atualizado para OCCUPIED");

        // 7. VERIFICAÇÃO DE CHAMADAS
        verify(tableRepository, times(1)).findById(tableId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(tableRepository, times(1)).save(any(RestaurantTable.class));
    }

    @Test
    @DisplayName("Deve Lançar uma exception de mesa ocupada")
    void shouldThrowExceptionIfTableAlreadyOccupied() {

        Long tableId = 5L;
        RestaurantTable occTable = new RestaurantTable();
        occTable.setId(tableId);
        occTable.setTableStatus(TableStatus.OCCUPIED);

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(occTable));

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> {
            service.openOrder(tableId);
        }, "Deveria lançar uma exception");

        String message = "A Mesa " + tableId + " já possui uma COMANDA ABERTA";

        verify(tableRepository, times(1)).findById(tableId);
        verify(orderRepository, never()).save(any());
        verify(tableRepository, never()).save(any());
        assertEquals(message, ex.getMessage(), "Mensagem deve corresponder");
    }

    @Test
    @DisplayName("Deve fechar a comanda e a mesa ficar livre")
    void shouldCloseOrderAndTableFree() {
        Long tableId = 5L;
        RestaurantTable occTable = new RestaurantTable();
        occTable.setId(tableId);
        occTable.setTableStatus(TableStatus.OCCUPIED);

        Order activeOrder = new Order();
        activeOrder.setId(10L);
        activeOrder.setTable(occTable);
        activeOrder.setOrderStatus(OrderStatus.OPEN);
        activeOrder.setTotalValue(new BigDecimal("50.00"));

        when(tableRepository.findById(tableId)).thenReturn(Optional.of(occTable));
        when(orderRepository.findByTableIdAndOrderStatus(tableId, OrderStatus.OPEN)).thenReturn(Optional.of(activeOrder));

        ArgumentCaptor<RestaurantTable> tableCaptor = ArgumentCaptor.forClass(RestaurantTable.class);
        when(tableRepository.save(tableCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(orderCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        service.closeOrder(tableId);

        RestaurantTable savedTable = tableCaptor.getValue();
        assertEquals(TableStatus.FREE, savedTable.getTableStatus(),
                "O status da mesa deve voltar para free");

        Order closedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.CLOSED, closedOrder.getOrderStatus(),
                "O status da comanda deve ser fechado");

        verify(tableRepository, times(1)).findById(tableId);
        verify(orderRepository, times(1)).findByTableIdAndOrderStatus(tableId, OrderStatus.OPEN);
        verify(tableRepository, times(1)).save(any(RestaurantTable.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
