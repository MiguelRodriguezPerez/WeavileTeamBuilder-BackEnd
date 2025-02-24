package com.example.demo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.AbilityData;
import com.example.demo.domain.PokemonData;
import com.example.demo.repositories.PokemonData_Repository;
import com.example.demo.services.interfaces.PokemonData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PokemonData_Service implements PokemonData_Interface{

    @Autowired
    PokemonData_Repository repo;

    @Autowired
    AbilityData_Service abilityData_Service;

    @Autowired
    MoveData_Service moveData_Service;

    @Override
    public PokemonData savePokemon(PokemonData pokemon) {
        return repo.save(pokemon);
    }

    @Override
    public void deleteAllPokemons() {
        repo.deleteAll();
    }

    @Override
    public PokemonData findPokemonByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public PokemonData requestPokemonFromPokeApi(short number) {
        
        PokemonData resultado = new PokemonData();
        JsonNode json_pokemon = ApiRequestManager.callGetRequest( "https://pokeapi.co/api/v2/pokemon/" + number);

        resultado = this.assignPokemonStats(resultado, json_pokemon);

        /* Este bucle asigna las habilidades al pokemón según el resultado del json */

        // for(JsonNode ability_json : json_pokemon.get("abilities")) {
        //     String current_ability_json = ability_json.get("ability").get("name").asText();
        //     AbilityData pokemonData_ability = abilityData_Service.getAbilityByName(current_ability_json);

        //     resultado.getAbility_list().add(pokemonData_ability);
        //     pokemonData_ability.getPokemon_list().add(resultado);
        // }

        // for(JsonNode move_json : json_pokemon.get("moves")) {
        //     String current_move_json = move_json.get("move").get("name").asText();
        //     MoveData pokemonData_move = moveData_Service.getMoveByName(current_move_json);


        // }
    }

    @Override
    public boolean requestAllPokemons() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestAllPokemons'");
    }

    @Override
    public PokemonData assignPokemonStats(PokemonData pokemonData, JsonNode pokemon_json) {
       
        for(JsonNode pokemon_stat_object : pokemon_json.get("stats")) {

            String current_type_stat = pokemon_stat_object.get("stat").get("name").asText();
            int current_base_stat = pokemon_stat_object.get("base_stat").asInt();

            switch(current_type_stat) {

                case "hp": 
                    pokemonData.setBase_hp((short) current_base_stat);
                    break;
                
                case "attack":
                    pokemonData.setBase_attack((short) current_base_stat);
                    break;
                
                case "defense":
                    pokemonData.setBase_defense((short) current_base_stat);
                    break;

                case "special-attack":
                    pokemonData.setBase_special_attack((short) current_base_stat);
                    break;
                
                case "special-defense":
                    pokemonData.setBase_special_defense((short) current_base_stat);
                    break;
                
                case "speed":
                    pokemonData.setBase_speed((short) current_base_stat);
                    break;

                default:
                    throw new RuntimeException("assignPokemonStats evalued a non-pokemon-stat. Faulty argument is : " + current_type_stat);
            }
        }

        return pokemonData;
    }
    
}
