package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.dto.ItemDataDto;
import com.example.demo.services.implementations.ItemData_Service;


@RequestMapping("/itemData")
@RestController
public class ItemDataController {

    @Autowired
    ItemData_Service itemData_Service;
    
    @GetMapping("/allItems")
    public ResponseEntity<Set<ItemDataDto>> getAllItemsEndpoint() {
        return new ResponseEntity<>(itemData_Service.getAllItemsAsDto(), HttpStatus.OK);
    }
    
}
