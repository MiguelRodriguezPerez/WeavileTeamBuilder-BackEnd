package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.pokemon.PokemonType;

@Repository
public interface PokemonTypeRepository extends JpaRepository <PokemonType, Long>{
    
}
