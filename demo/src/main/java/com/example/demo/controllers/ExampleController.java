package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.AbilityData_Service;

@RestController
public class ExampleController {

    @Autowired
    AbilityData_Service habilidadService;

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<>("hola", HttpStatus.OK);

    }

}
