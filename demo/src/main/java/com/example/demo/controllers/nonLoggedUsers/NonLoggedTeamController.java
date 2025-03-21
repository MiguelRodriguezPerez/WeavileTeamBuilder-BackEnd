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

    /* Recibir como argumento un enum directamente es muy complicado */
    @PostMapping("/createNewTeam")
    public ResponseEntity<PokemonTeam> postMethodName(@RequestBody JsonNode jsonNode) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");

        int teamTypeNumber = jsonNode.at("/teamType").asInt();

        return new ResponseEntity<>(new PokemonTeam(TeamType.fromValue(teamTypeNumber)),HttpStatus.CREATED);
    }

}
