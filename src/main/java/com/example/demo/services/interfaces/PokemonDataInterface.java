package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.dto.pokemon.PokemonDto;
import com.fasterxml.jackson.databind.JsonNode;

public interface PokemonDataInterface {

    PokemonData savePokemon(PokemonData pokemon);
    void deleteAllPokemons();
    Set<PokemonData> getAllPokemonData();
    PokemonData getPokemonByName(String name);
    PokemonDto convertPokemonDataToDto(PokemonData data);

    boolean requestAllPokemonsFromApi();
    PokemonData assignPokemonDataStats(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataMoves(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataSprites(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json);
    
}
