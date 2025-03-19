package com.example.demo.controllers.nonLoggedUsers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.team.PokemonTeam;
import com.example.demo.domain.team.TeamType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/nonLoggedUsers/pokemonTeam")
public class PokemonTeamController {
    
    @PostMapping("/createNewTeam")
    public ResponseEntity<PokemonTeam> postMethodName(@RequestBody TeamType teamType) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA");
        return new ResponseEntity<>(new PokemonTeam(teamType), HttpStatus.CREATED);
    }
    
}
