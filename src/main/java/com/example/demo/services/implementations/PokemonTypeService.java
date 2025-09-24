package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.services.interfaces.PokemonTypeInterface;

@Service
public class PokemonTypeService implements PokemonTypeInterface {

    @Autowired
    private DataSource dataSource;


    @Override
    public void requestAllTypesToApi() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestAllTypesToApi'");
    }

    @Override
    public PokemonType requesTypeToApi(int number) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requesTypeToApi'");
    }

    @Override
    @Transactional
    public PokemonType getTypeByName(String name) {
        String query = "SELECT * FROM pokemon_type WHERE name = ?";
        
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            
            return PokemonType.builder()
                        .id(resultSet.getLong("id"))
                        .nombre(resultSet.getString("name"))
                        .build();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
