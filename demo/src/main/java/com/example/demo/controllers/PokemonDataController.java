package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.pokemon.MissignoGridDTO;
import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.domain.pokemon.PokemonDataDTO;
import com.example.demo.services.implementations.PokemonData_Service;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("/pokemonData")
@RestController
public class PokemonDataController {

    @Autowired
    PokemonData_Service pokemonData_Service;
    
    @GetMapping("/allSVPokemon")
    public ResponseEntity<Set<MissignoGridDTO>> allSWPokemonEndpoint() {
        Set<MissignoGridDTO> resultado = pokemonData_Service.convertToMissignoGridDTO(pokemonData_Service.getAllSVPokemon());
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/getPokemonByName/{name}")
    public ResponseEntity<PokemonDataDTO> getPokemonDataByNameEndpoint(@PathVariable String name) {
        PokemonData pokemonData = pokemonData_Service.getPokemonByName(name);
        return new ResponseEntity<>(new PokemonDataDTO(pokemonData), HttpStatus.OK);
    }
    
    
}
