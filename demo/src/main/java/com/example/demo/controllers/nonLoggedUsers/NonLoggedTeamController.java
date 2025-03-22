package com.example.demo.controllers.nonLoggedUsers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.team.PokemonTeam;
import com.example.demo.domain.team.TeamType;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/nonLoggedUsers/pokemonTeam")
public class NonLoggedTeamController {

    /* Recibir como argumento un enum directamente es muy complicado. Tendrías que declarar un dto
    solo para recibir un enum. 

    En su lugar use la abstracción JsonNode de Jackson para recibir el json "directamente"
    y sacar el valor del enum ahí. Ten en cuenta que estoy obteniendo el número para decidir
    su valor y que es importante que tanto en el cliente como en el servidor los valores númericos
    del enum TeamType coincidan. */
    @PostMapping("/createNewTeam")
    public ResponseEntity<PokemonTeam> postMethodName(@RequestBody JsonNode jsonNode) {
        int teamTypeNumber = jsonNode.at("/teamType").asInt();

        return new ResponseEntity<>(new PokemonTeam(TeamType.fromValue(teamTypeNumber)),HttpStatus.CREATED);
    }

}
