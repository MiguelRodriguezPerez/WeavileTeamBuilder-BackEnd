package com.example.demo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.PokemonData;
import com.example.demo.repositories.PokemonData_Repository;
import com.example.demo.services.interfaces.PokemonData_Interface;

@Service
public class PokemonData_Service implements PokemonData_Interface{

    @Autowired
    PokemonData_Repository repo;

    @Override
    public PokemonData savePokemon(PokemonData pokemon) {
        return repo.save(pokemon);
    }

    @Override
    public void deleteAllPokemons() {
        repo.deleteAll();
    }

    @Override
    public PokemonData findPokemonByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public PokemonData requestPokemonFromPokeApi(int number) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestPokemonFromPokeApi'");
    }

    @Override
    public boolean requestAllPokemons() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestAllPokemons'");
    }
    
}
