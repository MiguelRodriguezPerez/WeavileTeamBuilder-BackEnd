package com.example.demo.services.implementations;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.PokemonType;
import com.example.demo.repositories.PokemonType_Repository;
import com.example.demo.services.interfaces.PokemonType_Interface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PokemonType_Service implements PokemonType_Interface {

    @Autowired
    PokemonType_Repository repo;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PokemonType saveType(PokemonType pokemonType) {
        return repo.save(pokemonType);
    }

    @Override
    public void deleteAllPokemonTypes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllPokemonTypes'");
    }

    @Override
    public PokemonType getTypeByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public PokemonType saveAllTypes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllTypes'");
    }

    @Override
    public Set<PokemonType> getAllPokemonType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPokemonType'");
    }

}
