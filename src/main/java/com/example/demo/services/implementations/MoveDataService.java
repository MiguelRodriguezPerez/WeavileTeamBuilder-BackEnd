package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.movements.MoveType;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.MoveDto;
import com.example.demo.repositories.MoveDataRepository;
import com.example.demo.services.interfaces.MoveDataInterface;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MoveDataService implements MoveDataInterface {

    @Autowired
    MoveDataRepository repo;

    @Autowired
    PokemonTypeService pokemonTypeService;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public MoveData saveMove(MoveData moveData) {
        return repo.save(moveData);
    }

    @Override
    public void deleteAllMoves() {
        repo.deleteAll();
    }

    @Override
    public MoveData getMoveByName(String name) {
        return repo.findByName(name);
    }

    @Transactional
    @Override
    public MoveData requestMoveToPokeApi(int number) {
        // Failed at 749
        MoveData resultado = new MoveData();
        JsonNode move_root = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/move/" + number);

        resultado.setName(move_root.get("name").asText());
        resultado.setAccuracy(move_root.get("accuracy").asInt());

        // Usa como referencia el valor del string a mayúsculas para definir el enum
        resultado.setMove_type(MoveType.valueOf(
                move_root.at("/damage_class/name").asText().toUpperCase()));

        // La multiplicación debería dar exacto, pero por si acaso lo paso a absoluto
        resultado.setPp((int) Math.abs(
                move_root.at("/pp").asInt() * 1.6));

        resultado.setPower(move_root.at("/power").asInt());

        resultado.setPokemon_type(
                pokemonTypeService.getTypeByName(move_root.at("/type/name").asText().toLowerCase()));

        /*
         * Algunos movimientos no tienen valores en effect_entries, y por tanto, no
         * tienen short_effect.
         * (Que es la descripción ideal competitiva del movimiento)
         * En ese caso lo mejor que puedes hacer es coger la descripción del movimiento
         * del juego en inglés
         */
        if (move_root.get("effect_entries").isEmpty()) {
            for (JsonNode flavor_text_entry : move_root.get("flavor_text_entries")) {
                if (flavor_text_entry.at("/language/name").asText().equals("en"))
                    resultado.setDescription(flavor_text_entry.at("/flavor_text").asText());
            }
        } else {
            resultado.setDescription(move_root.get("effect_entries")
                    .get(0)
                    .get("short_effect").asText());
        }

        return resultado;
    }

    @Override
    @Transactional
    @Modifying
    public boolean requestAllMovesToApi() {
        final int totalMovs = 919;
        final String sqlQuery = "INSERT INTO move_data (name, move_type, accuracy,"
                + "description, pp, power) VALUES (?, ?, ?, ?, ?, ?, ?)";
        HashMap<MoveData, PokemonType> typeRelations = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            for (int i = 1; i <= totalMovs; i++) {
                System.out.println("Movimiento " + i);

                MoveData currentMove = this.requestMoveToPokeApi(i);
                if (currentMove != null) {
                    preparedStatement.setString(1, currentMove.getName());
                    preparedStatement.setString(2, currentMove.getMove_type().toString());
                    preparedStatement.setInt(3, currentMove.getAccuracy());
                    preparedStatement.setString(4, currentMove.getDescription());
                    preparedStatement.setInt(5, currentMove.getPp());
                    preparedStatement.setInt(6, currentMove.getPower());

                    preparedStatement.addBatch();

                    typeRelations.put(currentMove, currentMove.getPokemon_type());
                }

                if (i % 100 == 0)
                    preparedStatement.executeBatch();
            }

            preparedStatement.executeBatch();

            this.createMoveDataPokemonTypeRelationship(typeRelations);

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Transactional
    @Modifying
    public boolean createMoveDataPokemonTypeRelationship(HashMap<MoveData, PokemonType> relationMap) {
        String query = "INSERT INTO move_data_move_type (move_data_id, pokemon_type_id) VALUES (?,?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (Map.Entry<MoveData, PokemonType> entry : relationMap.entrySet()) {
                preparedStatement.setLong(1, entry.getKey().getId());
                preparedStatement.setLong(2, entry.getValue().getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Set<MoveData> getMoveDataSetFromStringList(List<String> moveList) {
        return repo.getMoveDataSetFromStringList(moveList);
    }

    @Override
    public MoveDto convertMoveDataToDto(MoveData moveData) {
        return MoveDto.builder()
                .name(moveData.getName())
                .move_type(moveData.getMove_type().toString())
                .pokemon_type(moveData.getPokemon_type().getNombre())
                .power(moveData.getPower())
                .accuracy(moveData.getAccuracy())
                .description(moveData.getDescription())
                .pp(moveData.getPp())
                .build();
    }

}
