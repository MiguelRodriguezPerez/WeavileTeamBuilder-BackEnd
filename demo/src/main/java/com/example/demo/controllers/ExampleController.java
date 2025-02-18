package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.AbilityService;

@RestController
public class ExampleController {

    @Autowired
    AbilityService habilidadService;

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {

        habilidadService.requestSeveralAbilities(5, 8);

        return new ResponseEntity<>("hola", HttpStatus.OK);

    }

}
