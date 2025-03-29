package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.ItemData;
import com.example.demo.services.implementations.ItemData_Service;

@RestController
public class ExampleController {

    @Autowired
    ItemData_Service itemData_Service;

    @GetMapping("/")

    public Set<ItemData> getMethodName() {

        itemData_Service.requestAllItems();
        return itemData_Service.getAllItems();
    }

    // /* Prueba de que las imágenes van */
    // @GetMapping("/ver-imagen")
    // public ResponseEntity<byte[]> verImagen() {

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Content-Type", "image/png");
    //     return new ResponseEntity<>(pokemonData_Service.getPokemonById((long) 52).getFront_default_sprite(), headers,
    //             HttpStatus.OK);
    // }

}
