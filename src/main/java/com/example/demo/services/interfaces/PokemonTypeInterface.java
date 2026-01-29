package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.pokemon.PokemonType;

public interface PokemonTypeInterface {

    void requestAndSaveAllTypesFromApi();
    PokemonType requestTypeToApi(int number);
    PokemonType getTypeByName(String name);
    Set<PokemonType> getPokemonTypesByPokemonDataId(Long id);

}
