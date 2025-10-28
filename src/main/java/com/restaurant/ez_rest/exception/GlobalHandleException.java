package com.restaurant.ez_rest.exception;


import com.restaurant.ez_rest.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalHandleException {

    // --- A. TRATAMENTO DE EXCEÇÕES DE NEGÓCIO (404, 409) ---
    // 1. Exceções de "Não Encontrado" (TableNotFoundException) -> 404 NOT FOUND

    @ExceptionHandler({TableNotFoundException.class, ProductNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(RuntimeException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // 2. Exceções de "Conflito" (Ex: tentar abrir comanda em mesa ocupada) -> 409 CONFLICT
    @ExceptionHandler({ConflictNameException.class, BusinessLogicException.class})
    public ResponseEntity<ErrorResponseDTO> handleConflictException(RuntimeException ex) {
        log.warn("Conflito de Regra de negócio: {}", ex.getMessage());
        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

    // --- B. TRATAMENTO DE ERROS GENERICOS ---
    // 3. Qualquer outra exceção não tratada -> 500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        log.warn("Erro de Validação de Requisição: {}", ex.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // stats 500

        ErrorResponseDTO error = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}
