package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.PokemonData;

import jakarta.transaction.Transactional;

@Repository
public interface PokemonData_Repository extends JpaRepository<PokemonData, Long> {
    PokemonData findByName(String name);

    // Info en el sql del procedure
    @Procedure(name = "deleteAllPokemonProcedure")
    void deleteAllPokemonProcedure();

    @Query(value = "SELECT * FROM pokemon_data", nativeQuery = true)
    Set<PokemonData> getAllPokemonData();

    @Query(value = "SELECT * FROM pokemon_data pok WHERE pok.available_in_sv = true", nativeQuery = true)
    Set<PokemonData> getPokemonAvaliableInSV();
    
}
