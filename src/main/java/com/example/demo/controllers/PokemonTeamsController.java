package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.team.PokemonTeam;
import com.example.demo.services.implementations.PokemonTeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "PokemonTeam",
    description = "Endpoints related to Pokemon Teams CRUD actions"
)
@RestController
@RequestMapping("/nonLoggedUsers/pokemonTeam")
public class PokemonTeamsController {

    @Autowired
    PokemonTeamService team_service;

    @Operation(
        operationId = "createNewTeam",
        summary = "Create a new pokemon team",
        description = "Requests to create a new pokemon team. Will retrieve an empty team if succesfull"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Returns an empty pokemon team",
        content = @Content(
            schema = @Schema(implementation = PokemonTeam.class)
        )
    )
    @PostMapping("/createNewTeam")
    public ResponseEntity<PokemonTeam> postMethodName() {
        return new ResponseEntity<>(team_service.generateNewTeam(), HttpStatus.CREATED);
    }

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

}
