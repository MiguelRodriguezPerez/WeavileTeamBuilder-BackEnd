package com.example.demo.services.interfaces;

import com.example.demo.domain.PokemonData;
import com.fasterxml.jackson.databind.JsonNode;

public interface PokemonData_Interface {

    PokemonData savePokemon(PokemonData pokemon);
    void deleteAllPokemons();
    PokemonData findPokemonByName(String name);
    PokemonData requestPokemonFromPokeApi(short number);
    boolean requestAllPokemons();

    PokemonData assignPokemonStats(PokemonData pokemonData, JsonNode pokemon_json);

}

