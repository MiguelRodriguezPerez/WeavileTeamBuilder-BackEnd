package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.pokemon.MissignoGridDto;
import com.example.demo.dto.pokemon.PokemonDto;
import com.example.demo.services.implementations.PokemonDataService;

@RequestMapping("/pokemonData")
@RestController
public class PokemonDataController {

    @Autowired
    PokemonDataService pokemonDataService;

    @GetMapping("/allSVPokemon")
    public ResponseEntity<Set<MissignoGridDto>> getAllSWPokemonEndpoint() {
        Set<MissignoGridDto> resultado = pokemonDataService
                .convertToMissignoGridDTO(pokemonDataService.getAllSVPokemon());
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/getPokemonByName/{name}")
    public ResponseEntity<PokemonDto> getPokemonDataByNameEndpoint(@PathVariable String name) {
        PokemonDto dto = pokemonDataService.convertPokemonDataToDto(pokemonDataService.getPokemonByName(name));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
