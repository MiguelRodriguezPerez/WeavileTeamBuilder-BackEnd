package com.example.demo.services.implementations;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.config.ImageDownloader;
import com.example.demo.domain.AbilityData;
import com.example.demo.domain.PokemonData;
import com.example.demo.domain.PokemonType;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.repositories.PokemonData_Repository;
import com.example.demo.services.interfaces.PokemonData_Interface;
import com.fasterxml.jackson.databind.JsonNode;
/* Intente mover la construcción de PokemonData a una nueva clase, 
pero las inyecciones de spring (Concretamente una referencia circular) lo impidio

Si te preguntas porque los métodos no son estáticos 
es porque no puedes usar instancias de inyecciones en métodos estáticos

Estoy complementamente convencido de que existe 
una manera más eficiente de gestionar estas tareas que con jpa*/

@Service
public class PokemonData_Service implements PokemonData_Interface {

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
        repo.deleteAllPokemonProcedure();
    }

    @Override
    public Set<PokemonData> getAllPokemonData() {
        return repo.getAllPokemonData();
    }

    public PokemonData getPokemonById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public PokemonData getPokemonByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public PokemonData requestPokemonFromPokeApi(int number) {

        PokemonData resultado = new PokemonData();
        JsonNode json_pokemon = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/pokemon/" + number);

        resultado = this.createPokemonDataFromJson(resultado, json_pokemon);

        return resultado;
    }

    @Override
    public boolean requestAllPokemonsFromApi() {
        final int num_pokemon = 1025;

        for (int i = 899; i <= num_pokemon; i++) {
            System.out.println("Current pokemon: " + i);
            PokemonData pokemon = this.requestPokemonFromPokeApi(i);
            if (pokemon != null)
                this.savePokemon(pokemon);
        }

        return true;
    }

    public PokemonData createPokemonDataFromJson(PokemonData pokemonData, JsonNode pokemon_json) {

        pokemonData.setName(pokemon_json.get("name").asText());
        pokemonData = this.assignPokemonDataSprites(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataStats(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataAbilities(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataMoves(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataTypes(pokemonData, pokemon_json);

        return pokemonData;

    }

    public PokemonData assignPokemonDataStats(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode pokemon_stat_object : pokemon_json.at("/stats")) {

            String current_type_stat = pokemon_stat_object.at("/stat/name").asText();
            int current_base_stat = pokemon_stat_object.at("/base_stat").asInt();

            switch (current_type_stat) {

                case "hp":
                    pokemonData.setBase_hp(current_base_stat);
                    break;

                case "attack":
                    pokemonData.setBase_attack(current_base_stat);
                    break;

                case "defense":
                    pokemonData.setBase_defense(current_base_stat);
                    break;

                case "special-attack":
                    pokemonData.setBase_special_attack(current_base_stat);
                    break;

                case "special-defense":
                    pokemonData.setBase_special_defense(current_base_stat);
                    break;

                case "speed":
                    pokemonData.setBase_speed(current_base_stat);
                    break;

                default:
                    throw new RuntimeException(
                            "assignPokemonStats evalued a non-pokemon-stat. Faulty argument is : " + current_type_stat);
            }
        }

        return pokemonData;
    }

    public PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode ability_json : pokemon_json.get("abilities")) {

            String current_ability_json = ability_json.at("/ability/name").asText();
            AbilityData pokemonData_ability = abilityData_Service.getAbilityByName(current_ability_json);

            pokemonData.getAbility_list().add(pokemonData_ability);
            pokemonData_ability.getPokemon_list().add(pokemonData);

            this.savePokemon(pokemonData);
            abilityData_Service.saveAbility(pokemonData_ability);
        }

        return pokemonData;
    }

    public PokemonData assignPokemonDataMoves(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode move_json : pokemon_json.at("/moves")) {
            String current_move_name = move_json.at("/move/name").asText();
            MoveData current_MoveData = moveData_Service.getMoveByName(current_move_name);

            pokemonData.getMove_list().add(current_MoveData);
            current_MoveData.getPokemon_list().add(pokemonData);

            this.savePokemon(pokemonData);
            moveData_Service.saveMove(current_MoveData);
        }

        return pokemonData;
    }

    public PokemonData assignPokemonDataSprites(PokemonData pokemonData, JsonNode pokemon_json) {

        pokemonData.setFront_default_sprite(
                ImageDownloader.getImage(
                        pokemon_json.at("/sprites/front_default").asText()));

        if (!pokemon_json.at("/sprites/versions/generation-viii/icons/front_default").asText()
            .equals("null")) {
                pokemonData.setPc_sprite(
                    ImageDownloader.getImage(
                            pokemon_json.at("/sprites/versions/generation-viii/icons/front_default").asText()));
        }
        

        return pokemonData;
    }

    public PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json) {
        for (JsonNode current_type : pokemon_json.get("types")) {
            pokemonData.getType_list().add(
                    PokemonType.valueOf(current_type.at("/type/name")
                            .asText()
                            .toUpperCase()));
        }

        return pokemonData;
    }

}
