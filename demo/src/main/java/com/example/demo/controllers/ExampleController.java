package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.NatureData;
import com.example.demo.domain.PokemonData;
import com.example.demo.services.implementations.NatureData_Service;
import com.example.demo.services.implementations.PokemonData_Service;

@RestController
public class ExampleController {

    @Autowired
    NatureData_Service natureData_Service;

    @GetMapping("/")

    public Set<NatureData> getMethodName() {

        natureData_Service.requestAllNatures();
        return natureData_Service.getAllNatures();


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
