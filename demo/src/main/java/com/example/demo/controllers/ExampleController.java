package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.implementations.ItemData_Service;

@RestController
public class ExampleController {

    @Autowired
    ItemData_Service itemData_Service;

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {

        return new ResponseEntity<>("hola", HttpStatus.OK);

    }

    /* Prueba de que las imágenes van */
    @GetMapping("/ver-imagen")
    public ResponseEntity<byte[]> verImagen() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");
        return new ResponseEntity<>(itemData_Service.getItemByName("assault-vest").getImage_sprite(), headers, HttpStatus.OK);
    }

}
