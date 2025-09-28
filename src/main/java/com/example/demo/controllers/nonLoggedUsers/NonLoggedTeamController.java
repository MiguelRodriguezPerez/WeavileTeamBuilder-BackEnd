package com.example.demo.controllers.nonLoggedUsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.team.PokemonTeam;
import com.example.demo.services.implementations.PokemonTeamService;

@RestController
@RequestMapping("/nonLoggedUsers/pokemonTeam")
public class NonLoggedTeamController {

    @Autowired
    PokemonTeamService team_service;

    /*
     * Recibir como argumento un enum directamente es muy complicado. Tendrías que
     * declarar un dto
     * solo para recibir un enum.
     * 
     * En su lugar use la abstracción JsonNode de Jackson para recibir el json
     * "directamente"
     * y sacar el valor del enum ahí. Ten en cuenta que estoy obteniendo el número
     * para decidir
     * su valor y que es importante que tanto en el cliente como en el servidor los
     * valores númericos
     * del enum TeamType coincidan.
     */
    @PostMapping("/createNewTeam")
    public ResponseEntity<PokemonTeam> postMethodName() {
        return new ResponseEntity<>(team_service.generateNewTeam(), HttpStatus.CREATED);
    }

}
