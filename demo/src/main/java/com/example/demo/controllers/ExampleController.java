package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.ItemData_Service;
import com.example.demo.services.implementations.MoveData_Service;

@RestController
public class ExampleController {

    @Autowired
    ItemData_Service itemData_Service;

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        
        itemData_Service.requestAllItems();

        return new ResponseEntity<>("hola", HttpStatus.OK);

    }

}
