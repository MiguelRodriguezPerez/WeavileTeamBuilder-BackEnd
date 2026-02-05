package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.movements.MoveType;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.MoveDto;
import com.example.demo.dto.pokemon.PokemonTypeDto;
import com.example.demo.repositories.MoveDataRepository;
import com.example.demo.services.interfaces.MoveDataInterface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class MoveDataService implements MoveDataInterface {

    @Autowired
    MoveDataRepository repo;

    @Autowired
    PokemonTypeService pokemonTypeService;

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
                pokemonTypeService.getTypeByName(move_root.at("/type/name").asText().toLowerCase())
            );

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
    public boolean requestAndSaveAllMovesFromApi() {
        final int totalMovs = 919;
        final String sqlQuery = "INSERT INTO move_data (name, move_type, accuracy,"
                + "description, pp, power, pokemon_type_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                    preparedStatement.setLong(7, currentMove.getPokemon_type().getId());

                    preparedStatement.addBatch();

                    typeRelations.put(currentMove, currentMove.getPokemon_type());
                }

                if (i % 100 == 0)
                    preparedStatement.executeBatch();
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

    public Set<MoveDto> getPokemonMovesByPokemonId (Long pokemonId) {
        Set<MoveDto> resultado = new HashSet<>();

        String movesQuery = """
                SELECT
                    mov.id          AS move_id,
                    mov.accuracy    AS move_accuracy,
                    mov.description AS move_description,
                    mov.move_type   AS move_move_type,
                    mov.name        AS move_name,
                    mov.power       AS move_power,
                    mov.pp          AS move_pp,

                    ty.id           AS type_id,
                    ty.name         AS type_name,
                    ty.sprite       AS type_sprite
                FROM pokemon_data_move_data pdmd
                INNER JOIN move_data mov ON mov.id = pdmd.move_data_id
                INNER JOIN pokemon_type ty ON ty.id = mov.pokemon_type_id
                WHERE pdmd.pokemon_data_id = ?
                """;

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(movesQuery);
            ps.setLong(1, pokemonId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado.add(
                    MoveDto.builder()
                        .id(rs.getLong("move_id"))
                        .name(rs.getString("move_name"))
                        .move_type(
                            MoveType.valueOf(rs.getString("move_move_type"))
                        )
                        .power(rs.getInt("move_power"))
                        .accuracy(rs.getInt("move_accuracy"))
                        .description(rs.getString("move_description"))
                        .pp(rs.getInt("move_pp"))
                        .pokemon_type(
                            PokemonTypeDto.builder()
                                .id(rs.getLong("type_id"))
                                .name(rs.getString("type_name"))
                                .build()
                        )
                        .build()
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultado;
    }


}
