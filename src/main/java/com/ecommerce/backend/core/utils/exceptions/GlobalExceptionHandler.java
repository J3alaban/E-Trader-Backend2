package com.ecommerce.backend.core.utils.exceptions;

import com.ecommerce.backend.dtos.responses.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        // Hata detaylarını oluştur
        ExceptionDetails errorDetails = new ExceptionDetails(
                ex.getMessage(), // Exception mesajını al
                request.getDescription(false) // WebRequest detaylarını al
        );

        // ResponseEntity ile geri döndür
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
