package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.PokemonType;

public interface PokemonType_Interface {

    PokemonType saveType(PokemonType pokemonType);
    void deleteAllPokemonTypes();
    PokemonType getTypeByName(String name);
    PokemonType saveAllTypes();
    Set<PokemonType> getAllPokemonType();

}
