package com.example.demo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.PokemonType;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.movements.MoveType;
import com.example.demo.repositories.MoveData_Repository;
import com.example.demo.services.interfaces.MoveData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class MoveData_Service implements MoveData_Interface {

    @Autowired
    MoveData_Repository repo;

    @Override
    public MoveData saveMove(MoveData moveData) {
        return repo.save(moveData);
    }

    @Override
    public void deleteAllMoves() {
        repo.deleteAll();
    }

    @Override
    public MoveData findMoveByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public MoveData requestMoveToPokeApi(int number) {
        // Failed at 749

        MoveData resultado = new MoveData();
        JsonNode move_request = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/move/" + number);

        System.out.println(move_request.get("name").asText());
        resultado.setName(move_request.get("name").asText());
        resultado.setAccuracy((byte) move_request.get("accuracy").asInt());
        resultado.setDescription(move_request.get("effect_entries").get(0).get("short_effect").asText());
        // Usa como referencia el valor del string a mayúsculas para definir el enum
        resultado.setMove_type(MoveType.valueOf(move_request.get("damage_class").get("name").asText().toUpperCase()));
        resultado.setPokemon_type(PokemonType.valueOf(move_request.get("type").get("name").asText().toUpperCase()));
        resultado.setPp((byte) Math.abs(move_request.get("pp").asInt() * 1.6));


        return resultado;
    }

    @Override
    public boolean requestAllMoves() {
        final int totalMovs = 919;
        // Failed at 749
        for(int i = 749; i <= totalMovs; i++) {
            System.out.println("Movimiento " + i);
            this.saveMove(this.requestMoveToPokeApi(i));
        }

        return true;
    }

}
