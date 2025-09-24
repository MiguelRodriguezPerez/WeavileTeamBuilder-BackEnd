package com.example.demo.services.interfaces;

import com.example.demo.domain.pokemon.PokemonType;

public interface PokemonTypeInterface {
    
    void requestAllTypesToApi();
    PokemonType requesTypeToApi(int number);
    PokemonType getTypeByName(String name);

}
