package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.PokemonType;


public interface PokemonType_Repository extends JpaRepository <PokemonType,Long>{
    
    PokemonType findByName(String name);
}
