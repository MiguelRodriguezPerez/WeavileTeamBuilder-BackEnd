package com.example.demo.controllers.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.domain.WeavileErrorResponse;
import com.example.demo.exceptions.ItemNotFoundException;

@RestControllerAdvice
public class ItemControllerAdvice {
    
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<WeavileErrorResponse> handleItemNotFound(ItemNotFoundException ex) {
        return ResponseEntity.status(404)
            .body(
                WeavileErrorResponse.builder()
                .message(ex.getMessage())
                .date(LocalDate.now())
                .build()
            );
    }
}
