package com.restaurant.ez_rest;


import com.restaurant.ez_rest.dto.tableDTOs.TableRequestDTO;
import com.restaurant.ez_rest.dto.tableDTOs.TableResponseDTO;
import com.restaurant.ez_rest.model.RestaurantTable;
import com.restaurant.ez_rest.model.TableStatus;
import com.restaurant.ez_rest.repository.TableRepository;
import com.restaurant.ez_rest.service.TableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private TableRepository repository;

    @InjectMocks
    private TableService service;


    @Test
    @DisplayName("Deve Criar uma mesa vazia")
    void shouldCreateNewFreeTable() {
        RestaurantTable table = new RestaurantTable();
        TableRequestDTO requestDTO = new TableRequestDTO();

        // primeiro save
        when(repository.save(any(RestaurantTable.class))).thenAnswer(invocationOnMock -> {
            RestaurantTable saved = invocationOnMock.getArgument(0);
            saved.setId(1L);
            return saved;
        })

        // segundo save
                .thenAnswer(invocation -> {
                    RestaurantTable finalSaved = invocation.getArgument(0);
                    assertNotNull(finalSaved.getQrCodeUrl(), "O QR code deve ser gerado");
                    return finalSaved;
                });

        TableResponseDTO responseDTO = service.createTable(requestDTO);

        assertNotNull(responseDTO, "A mesa salva dete ter um ID");
        assertNotNull(responseDTO.id(), "A mesa deve contar um ID");
        assertEquals(TableStatus.FREE, responseDTO.tableStatus(), "O status da mesa deve ser o mesmo");
        assertNotNull(responseDTO.qrCodeUrl(), "A URL do QRCODE DEVE ESTAR PRESENT");
        verify(repository, times(2)).save(any(RestaurantTable.class));
    }
}
