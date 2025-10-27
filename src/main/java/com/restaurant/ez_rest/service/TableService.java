package com.restaurant.ez_rest.service;


import com.restaurant.ez_rest.dto.tableDTOs.TableRequestDTO;
import com.restaurant.ez_rest.dto.tableDTOs.TableResponseDTO;
import com.restaurant.ez_rest.exception.TableNotFoundException;
import com.restaurant.ez_rest.model.Table;
import com.restaurant.ez_rest.model.TableStatus;
import com.restaurant.ez_rest.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    @Value("${api.base.url:http://localhost:8080/api/v1}")
    private String apiBaseUrl;


    public TableResponseDTO createTable(TableRequestDTO requestDTO) {

        Table nTable = new Table();
        nTable.setTableStatus(TableStatus.FREE);

        Table savedTable = tableRepository.save(nTable);

        String qrCodeUrl = generateQRCodeUrl(savedTable.getId());
        savedTable.setQrCodeUrl(qrCodeUrl);

        Table finalTable = tableRepository.save(savedTable);

        return TableResponseDTO.fromModel(finalTable);
    }

    private String generateQRCodeUrl(long tableId) {
        return apiBaseUrl + "/extrato/" + tableId;
    }

    public List<TableResponseDTO> listAllTables() {
        return tableRepository.findAll().stream().map(TableResponseDTO::fromModel).toList();
    }

    public TableResponseDTO listById(Long id) {
        Table table = tableRepository.findById(id).orElseThrow(() -> new TableNotFoundException("Mesa com ID: "+id+", não encontrada"));

        return TableResponseDTO.fromModel(table);
    }

    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new TableNotFoundException("Mesa com ID: "+id+", não encontrada");
        }
        tableRepository.deleteById(id);
    }

}
