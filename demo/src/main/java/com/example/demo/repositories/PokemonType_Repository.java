package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.PokemonType;


public interface PokemonType_Repository extends JpaRepository <PokemonType,Long>{
    
    PokemonType findByName(String name);
    
    @Query(value = "SELECT * FROM pokemon_type", nativeQuery = true)
    Set<PokemonType> getAllPokemonTypes();
}
