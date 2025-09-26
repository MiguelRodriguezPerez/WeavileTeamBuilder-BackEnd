package com.example.demo.services.interfaces;

import com.example.demo.domain.pokemon.PokemonType;

public interface PokemonTypeInterface {

    void requestAndSaveAllTypesFromApi();

    PokemonType requestTypeToApi(int number);

    PokemonType getTypeByName(String name);

}
