package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.team.NatureData;
import com.example.demo.services.implementations.NatureData_Service;


@RequestMapping("/natureData")
@RestController
public class NatureDataController {

    @Autowired
    NatureData_Service natureData_Service;
    
    @GetMapping("/getNatureByName/{name}")
    public ResponseEntity<NatureData> getNatureByNameEndpoint(@PathVariable String name) {
        return new ResponseEntity<>(natureData_Service.getNatureByName(name), HttpStatus.OK);
    }
    
}
