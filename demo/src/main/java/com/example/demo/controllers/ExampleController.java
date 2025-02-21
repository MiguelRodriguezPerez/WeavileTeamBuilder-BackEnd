package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.MoveData_Service;

@RestController
public class ExampleController {

    @Autowired
    MoveData_Service moveData_Service;

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        moveData_Service.requestAllMoves();

        return new ResponseEntity<>("hola", HttpStatus.OK);

    }

}
