package com.example.demo.controllers;

<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> movimientos
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.ItemData;
import com.example.demo.services.implementations.AbilityData_Service;
import com.example.demo.services.implementations.ItemData_Service;
import com.example.demo.services.implementations.PokemonData_Service;

@RestController
public class ExampleController {

    @Autowired
    PokemonData_Service pokemonData_Service;

    @GetMapping("/")
    public Set<AbilityData> getMethodName() {

        pokemonData_Service.requestAllPokemonsFromApi();

    }

    /* Prueba de que las imágenes van */
    @GetMapping("/ver-imagen")
    public ResponseEntity<byte[]> verImagen() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");
        return new ResponseEntity<>(pokemonData_Service.getPokemonById((long) 52).getFront_default_sprite(), headers,
                HttpStatus.OK);
    }

}
