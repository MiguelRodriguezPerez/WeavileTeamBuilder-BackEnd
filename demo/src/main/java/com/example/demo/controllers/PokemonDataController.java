package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.services.implementations.PokemonData_Service;


@RequestMapping("/pokemonData")
@RestController
public class PokemonDataController {

    @Autowired
    PokemonData_Service pokemonData_Service;
    
    @GetMapping("/allSVPokemon")
    public ResponseEntity<Set<PokemonData>> allSWPokemonEndpoint() {
        return new ResponseEntity<>(pokemonData_Service.getAllSVPokemon(), HttpStatus.OK);
    }
    
}
