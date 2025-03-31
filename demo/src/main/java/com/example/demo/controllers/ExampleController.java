package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.PokemonData;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.services.implementations.AbilityData_Service;
import com.example.demo.services.implementations.MoveData_Service;
import com.example.demo.services.implementations.NatureData_Service;
import com.example.demo.services.implementations.PokemonData_Service;

@RestController
public class ExampleController {

    @Autowired
    NatureData_Service natureData_Service;

    @GetMapping("/")
    public void getMethodName() {
        natureData_Service.requestAllNatures();
    }

    // /* Prueba de que las imágenes van */
    // @GetMapping("/ver-imagen")
    // public ResponseEntity<byte[]> verImagen() {

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Content-Type", "image/png");
    //     return new ResponseEntity<>(itemData_Service.getItemById((long) 52).getImage_sprite(), headers,
    //             HttpStatus.OK);
    // }

}
