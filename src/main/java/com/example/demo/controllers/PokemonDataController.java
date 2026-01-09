package com.example.demo.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.apiResponses.ApiNotFoundResponse;
import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.dto.pokemon.MissignoDto;
import com.example.demo.dto.pokemon.PokemonDto;
import com.example.demo.services.implementations.PokemonDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pokemon Data", description = "Endpoints to return all relevant data about pokemon")
@RequestMapping("/pokemonData")
@RestController
public class PokemonDataController {

    @Autowired
    PokemonDataService pokemonDataService;

    @Operation(operationId = "allSVPokemon", summary = "Retrieve all available pokemon in sv", description = "Returns a list of pokemon data which are available in pokemon sv")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request with all pokemon data", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MissignoDto.class))))
    })
    @GetMapping("/allSVPokemon")
    public ResponseEntity<Set<MissignoDto>> getAllSWPokemonEndpoint() {
        Set<MissignoDto> resultado = pokemonDataService
                .convertToMissignoGridDTO(pokemonDataService.getAllSVPokemon());
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Operation(operationId = "getPokemonByName", summary = "Get pokemonData by name", description = "Request a certain pokemonData by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succesfully retrieved pokemon", content = @Content(contentSchema = @Schema(implementation = PokemonData.class)))
    })
    @ApiNotFoundResponse
    @GetMapping("/getPokemonByName/{name}")
    public ResponseEntity<PokemonDto> getPokemonDataByNameEndpoint(@PathVariable String name) {
        PokemonDto dto = pokemonDataService.convertPokemonDataToDto(pokemonDataService.getPokemonByName(name));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
