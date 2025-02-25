package com.example.demo.services.interfaces;

import com.example.demo.domain.PokemonData;

public interface PokemonData_Interface {

    PokemonData savePokemon(PokemonData pokemon);

    void deleteAllPokemons();

    PokemonData getPokemonByName(String name);

    PokemonData requestPokemonFromPokeApi(int number);

    boolean requestAllPokemons();

}
