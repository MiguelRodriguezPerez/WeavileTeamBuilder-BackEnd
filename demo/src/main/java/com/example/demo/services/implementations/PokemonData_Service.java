package com.example.demo.services.implementations;

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
/* Intente mover la construcción de PokemonData a una nueva clase, pero las inyecciones de spring (Concretamente una referencia circular)
lo impidio

Si te preguntas porque los métodos no son estáticos es porque no puedes usar instancias de inyecciones en métodos estáticos

Estoy complementamente convencido de que existe una manera más eficiente de gestionar estas tareas que con jpa*/

import jakarta.transaction.Transactional;

/* Para asegurarte de que las inyecciones funcionen tienes que anotar esta clase como un Component */
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
    public boolean requestAllPokemons() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestAllPokemons'");
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
    
    
    private PokemonData assignPokemonDataStats(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode pokemon_stat_object : pokemon_json.get("stats")) {

            String current_type_stat = pokemon_stat_object.get("stat").get("name").asText();
            int current_base_stat = pokemon_stat_object.get("base_stat").asInt();

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

    private PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode ability_json : pokemon_json.get("abilities")) {

            String current_ability_json = ability_json.get("ability").get("name").asText();
            AbilityData pokemonData_ability = abilityData_Service.getAbilityByName(current_ability_json);

            pokemonData.getAbility_list().add(pokemonData_ability);
            pokemonData_ability.getPokemon_list().add(pokemonData);

            this.savePokemon(pokemonData);
            abilityData_Service.saveAbility(pokemonData_ability);
        }

        return pokemonData;
    }

    private PokemonData assignPokemonDataMoves(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode move_json : pokemon_json.get("moves")) {
            String current_move_name = move_json.get("move").get("name").asText();
            MoveData current_MoveData = moveData_Service.getMoveByName(current_move_name);

            pokemonData.getMove_list().add(current_MoveData);
            current_MoveData.getPokemon_list().add(pokemonData);

            this.savePokemon(pokemonData);
            moveData_Service.saveMove(current_MoveData);
        }

        return pokemonData;
    }

    private PokemonData assignPokemonDataSprites(PokemonData pokemonData, JsonNode pokemon_json) {
        JsonNode sprites = pokemon_json.get("sprites");

        pokemonData.setFront_default_sprite(ImageDownloader.getImage(sprites.get("front_default").asText()));
        pokemonData.setPc_sprite(ImageDownloader.getImage(sprites.get("versions")
                .get("generation-viii")
                .get("icons")
                .get("front_default").asText()));

        return pokemonData;
    }

    private PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json) {

        for (JsonNode current_type : pokemon_json.get("types")) {
            String new_type = current_type.get("type").get("name").asText();
            pokemonData.getType_list().add(PokemonType.valueOf(new_type.toUpperCase()));
        }

        return pokemonData;
    }
}
