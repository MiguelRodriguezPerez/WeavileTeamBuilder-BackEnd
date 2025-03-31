package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.PokemonData;
import com.fasterxml.jackson.databind.JsonNode;

public interface PokemonData_Interface {

    PokemonData savePokemon(PokemonData pokemon);
    void deleteAllPokemons();
    Set<PokemonData> getAllPokemonData();
    PokemonData getPokemonByName(String name);
    JsonNode requestPokemonFromPokeApi(int number);
    boolean requestAllPokemonsFromApi();

    PokemonData assignPokemonDataStats(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataMoves(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataSprites(PokemonData pokemonData, JsonNode pokemon_json);
    PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json);
}
