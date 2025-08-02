package com.example.demo.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.pokemon.PokemonData;

@Repository
public interface PokemonDataRepository extends JpaRepository<PokemonData, Long> {
    PokemonData findByName(String name);

    // Info en el sql del procedure
    @Procedure(name = "deleteAllPokemonProcedure")
    void deleteAllPokemonProcedure();

    @Query(value = "SELECT * FROM pokemon_data", nativeQuery = true)
    Set<PokemonData> getAllPokemonData();

    // No esta muy claro que sea más rápida que findByAvailableInSv (EntityManager)
    /* No puedes solicitar el dto directamente porque este dto contiene colecciones que nacen 
     * de relaciones con otras entidades. 
     * Lo normal es lo que estas haciendo, obtener las entidades y convertirlas a dto 
     * en el servicio
     */
    @Query(value = "SELECT * FROM pokemon_data pok WHERE pok.available_in_sv = true", nativeQuery = true)
    Set<PokemonData> getPokemonAvaliableInSV();

    // List<PokemonData> findByAvailableInSv(Boolean availableInSv);
}
