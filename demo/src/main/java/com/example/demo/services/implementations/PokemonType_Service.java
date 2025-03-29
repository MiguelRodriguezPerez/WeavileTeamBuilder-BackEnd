package com.example.demo.services.implementations;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.CsvFileManager;
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

    @Autowired
    CsvFileManager csvFileManager;

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
    @Transactional
    @Modifying
    public Set<PokemonType> saveAllTypes() {
        String sqlQuery = "INSERT INTO pokemon_type (name) VALUES (?)";
        List<String> typeList = csvFileManager.getCsvList("csvFolder/pokemonTypes.csv");

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                for (String type : typeList) {
                    ps.setString(1, type);
                    ps.addBatch();
                }

                ps.executeBatch();
            }
        });

        return this.getAllPokemonTypes();
    }

    @Override
    public Set<PokemonType> getAllPokemonTypes() {
        return repo.getAllPokemonTypes();
    }

}
