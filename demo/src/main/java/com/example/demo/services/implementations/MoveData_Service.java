package com.example.demo.services.implementations;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.PokemonType;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.movements.MoveType;
import com.example.demo.repositories.MoveData_Repository;
import com.example.demo.services.interfaces.MoveData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MoveData_Service implements MoveData_Interface {

    @Autowired
    MoveData_Repository repo;

    @PersistenceContext
    EntityManager entityManager;

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
                move_root.at("/damage_class/name").asText().toUpperCase()
            )
        );
        resultado.setPokemon_type(PokemonType.valueOf(
                move_root.at("/type/name").asText().toUpperCase()
            )
        );

        // La multiplicación debería dar exacto, pero por si acaso lo paso a absoluto
        resultado.setPp((int) Math.abs(
                move_root.at("/pp").asInt() * 1.6));

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
        } 
        else {
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
        final String sqlQuery = "INSERT INTO move_data (name, move_type, pokemon_type, accuracy,"
            + "description, pp) VALUES (?, ?, ?, ?, ?, ?)";

        entityManager.unwrap(Session.class).doWork(connection -> {
            try(PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                for (int i = 1; i <= totalMovs; i++) {
                    System.out.println("Movimiento " + i);

                    MoveData currentMove = this.requestMoveToPokeApi(i);
                    if (currentMove != null) {
                        ps.setString(1, currentMove.getName());
                        ps.setString(2, currentMove.getMove_type().toString());
                        ps.setString(3, currentMove.getPokemon_type().toString());
                        ps.setInt(4, currentMove.getAccuracy());
                        ps.setString(5, currentMove.getDescription());
                        ps.setInt(6, currentMove.getPp());


                        ps.addBatch();
                    }
                }

                ps.executeBatch();
            }
        });
        

        return true;
    }

    @Override
    public Set<MoveData> getAllMoveData() {
        return repo.getAllMoveData();
    }

    @Transactional
    public Set<MoveData> getMoveDataSetFromStringList(List<String> moveList) {
        return repo.getMoveDataSetFromStringList(moveList);
    }

}
