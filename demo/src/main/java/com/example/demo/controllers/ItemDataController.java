package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ItemDto;
import com.example.demo.services.implementations.ItemDataService;

@RequestMapping("/itemData")
@RestController
public class ItemDataController {

    @Autowired
    ItemDataService itemDataService;

    @GetMapping("/allItems")
    public ResponseEntity<Set<ItemDto>> getAllItemsEndpoint() {
        return new ResponseEntity<>(itemDataService.getAllItemsAsDto(), HttpStatus.OK);
    }

}
