package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.PokemonType;

public interface PokemonType_Interface {

    void deleteAllPokemonTypes();
    PokemonType getTypeByName(String name);
    Set<PokemonType> saveAllTypes();
    Set<PokemonType> getAllPokemonTypes();

}
