package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.PokemonData;

@Repository
public interface PokemonData_Repository extends JpaRepository<PokemonData, Long> {
    PokemonData findByName(String name);

    // Info en el sql del procedure
    @Procedure(name = "deleteAllPokemonProcedure")
    void deleteAllPokemonProcedure();
}
