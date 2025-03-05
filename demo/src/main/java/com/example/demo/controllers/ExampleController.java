package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.movements.MoveData;
import com.example.demo.services.implementations.MoveData_Service;

@RestController
public class ExampleController {

    @Autowired
    MoveData_Service moveData_Service;

    @GetMapping("/")
    public Set<MoveData> getMethodName() {
        moveData_Service.requestAllMovesToApi();

        return moveData_Service.getAllMoveData();

    }

}
