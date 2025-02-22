package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.PokemonData;

@Repository
public interface PokemonData_Repository extends JpaRepository<PokemonData,Long>{
    PokemonData findByName(String name);
}
