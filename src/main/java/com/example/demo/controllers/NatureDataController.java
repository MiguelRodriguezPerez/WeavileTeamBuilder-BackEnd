package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NatureDto;
import com.example.demo.services.implementations.NatureDataService;

@RequestMapping("/natureData")
@RestController
public class NatureDataController {

    @Autowired
    NatureDataService natureDataService;

    @GetMapping("/getNatureByName/{name}")
    public ResponseEntity<NatureDto> getNatureByNameEndpoint(@PathVariable String name) {
        System.out.println(name+ "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        return new ResponseEntity<>(
            natureDataService.convertNatureDataToDto(natureDataService.getNatureByName(name)), HttpStatus.OK);
    }

    @GetMapping("/getAllNatures")
    public ResponseEntity<Set<NatureDto>> getAllNaturesEndpoint() {
        return new ResponseEntity<Set<NatureDto>>(natureDataService.getAllNaturesAsDto(), HttpStatus.OK);
    }

}
