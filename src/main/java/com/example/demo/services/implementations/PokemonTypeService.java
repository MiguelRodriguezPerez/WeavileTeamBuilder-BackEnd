package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.services.interfaces.PokemonTypeInterface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PokemonTypeService implements PokemonTypeInterface {

    @Autowired
    private DataSource dataSource;

    @Override
    public void requestAndSaveAllTypesFromApi() {
        /* Ignorar stellar */
        final int totalTypes = 18;
        final String query = "INSERT INTO pokemon_type (nombre,sprite) VALUES (?,?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 18; i <= totalTypes; i++) {
                System.out.println("Tipo " + i);
                PokemonType pokemonType = this.requestTypeToApi(i);

                preparedStatement.setString(1, pokemonType.getName());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public PokemonType requestTypeToApi(int number) {
        String request = "https://pokeapi.co/api/v2/type/" + number;
        PokemonType pokemonType = new PokemonType();
        JsonNode typeNode = ApiRequestManager.callGetRequest(request);

        pokemonType.setName(typeNode.at("/name").asText());

        return pokemonType;
    }

    @Override
    @Transactional
    public PokemonType getTypeByName(String name) {
        String query = "SELECT * FROM pokemon_type WHERE nombre = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return PokemonType.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .build();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<PokemonType> getPokemonTypesByPokemonDataId(Long id) {
        Set<PokemonType> resultado = new HashSet<>();

        String query = """
            SELECT pt.id, pt.name, pt.sprite
            FROM pokemon_data_pokemon_type pdpt
            INNER JOIN pokemon_type pt ON pdpt.pokemon_type_id = pt.id
            WHERE pdpt.pokemon_data_id = ?
        """;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado.add(
                    PokemonType.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .build()
                );
            }

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultado;
    }

}
