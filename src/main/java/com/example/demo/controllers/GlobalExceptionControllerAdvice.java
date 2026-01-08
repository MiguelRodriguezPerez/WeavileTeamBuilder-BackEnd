package com.example.demo.controllers;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.domain.WeavileErrorResponse;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.exceptions.NatureNotFoundException;
import com.example.demo.exceptions.PokemonNotFoundException;

/* Las excepciones not found de cada entidad se controlan por separado, con una excepción personalizada
por cada una, pero se devuelve el mensaje del error de la excepción en un objeto único para todos
los errores de solicitudes */

@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler({
            ItemNotFoundException.class,
            NatureNotFoundException.class,
            PokemonNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<WeavileErrorResponse> handleItemNotFound(RuntimeException ex) {
        return ResponseEntity.status(404)
                .body(
                    WeavileErrorResponse.builder()
                            .message(ex.getMessage())
                            .date(LocalDate.now())
                            .build()
                );
    }
}
