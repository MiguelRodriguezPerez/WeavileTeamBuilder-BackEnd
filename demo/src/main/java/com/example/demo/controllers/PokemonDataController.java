package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.pokemon.MissignoGridDTO;
import com.example.demo.services.implementations.PokemonData_Service;


@RequestMapping("/pokemonData")
@RestController
public class PokemonDataController {

    @Autowired
    PokemonData_Service pokemonData_Service;
    
    // Este endpoint tarda 6 segundos en mostrar la info. Seguro que hay una solución más rápida
    @GetMapping("/allSVPokemon")
    public ResponseEntity<Set<MissignoGridDTO>> allSWPokemonEndpoint() {
        Set<MissignoGridDTO> resultado = pokemonData_Service.convertToMissignoGridDTO(pokemonData_Service.getAllSVPokemon());
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
    
}
