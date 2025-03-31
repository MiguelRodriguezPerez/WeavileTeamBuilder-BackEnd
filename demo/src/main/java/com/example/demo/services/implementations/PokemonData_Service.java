package com.example.demo.services.implementations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.config.CsvFileReader;
import com.example.demo.config.ImageDownloader;
import com.example.demo.domain.AbilityData;
import com.example.demo.domain.PokemonData;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.repositories.PokemonData_Repository;
import com.example.demo.services.interfaces.PokemonData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Service
public class PokemonData_Service implements PokemonData_Interface {

    @Autowired
    PokemonData_Repository repo;

    @Autowired
    AbilityData_Service abilityData_Service;

    @Autowired
    MoveData_Service moveData_Service;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CsvFileReader csvFileReader;

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
    @Transactional
    @Modifying
    public boolean requestAllPokemonsFromApi() {
        final int num_pokemon = 1025;
        
        for (int i = 1; i <= num_pokemon; i++) {
            System.out.println("Current pokemon: " + i);
            
            JsonNode pokemonJson = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/pokemon/" + i);
            PokemonData pokemon = this.savePokemonDataFromJson(pokemonJson);

            this.createPokemonDataRelations(pokemon, pokemonJson);
        }

        return true;
    }

    @Transactional
    @Modifying
    public PokemonData savePokemonDataFromJson(JsonNode pokemon_json) {
        PokemonData tempPokemonData = new PokemonData();

        tempPokemonData.setName(pokemon_json.get("name").asText());
        tempPokemonData = this.assignPokemonDataSprites(tempPokemonData, pokemon_json);
        tempPokemonData = this.assignPokemonDataStats(tempPokemonData, pokemon_json);
    
        final PokemonData pokemonData = tempPokemonData; // Variable final para usar en la lambda

        final String firstQuery = "INSERT INTO pokemon_data (name, base_hp, base_attack, base_defense" 
            + ", base_special_attack, base_special_defense, base_speed" 
            + ", front_default_sprite, pc_sprite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        entityManager.unwrap(Session.class).doWork(connection -> {
            try(PreparedStatement ps = connection.prepareStatement(firstQuery, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, pokemonData.getName());
                ps.setInt(2, pokemonData.getBase_hp());
                ps.setInt(3, pokemonData.getBase_attack());
                ps.setInt(4, pokemonData.getBase_defense());
                ps.setInt(5, pokemonData.getBase_special_attack());
                ps.setInt(6, pokemonData.getBase_special_defense());
                ps.setInt(7, pokemonData.getBase_speed());
                ps.setBytes(8, pokemonData.getFront_default_sprite());
                ps.setBytes(9, pokemonData.getPc_sprite());

                ps.executeUpdate(); 

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) pokemonData.setId(generatedKeys.getLong(1)); // Asignamos el ID generado
                    else throw new SQLException("Error al obtener el ID generado para pokemon_data");
                }

            }
        });

        return pokemonData;

    }

    @Transactional
    public PokemonData createPokemonDataRelations(PokemonData pokemonData, JsonNode pokemon_json) {
        
        pokemonData = this.assignPokemonDataAbilities(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataMoves(pokemonData, pokemon_json);
        pokemonData = this.assignPokemonDataTypes(pokemonData, pokemon_json);

        return pokemonData;
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    @Modifying
    public PokemonData assignPokemonDataMoves(PokemonData pokemonData, JsonNode pokemon_json) {
        List<String> moveNameList = new ArrayList<>();
        for (JsonNode move_json : pokemon_json.at("/moves")) {
            moveNameList.add(move_json.at("/move/name").asText());
        }

        Set<MoveData> definitiveMoveSet = moveData_Service.getMoveDataSetFromStringList(moveNameList);

        String sqlQuery = "INSERT INTO `pokemon_data-move_data` (pokemon_data_id, move_data_id) VALUES (?, ?)";

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                for (MoveData move : definitiveMoveSet) {
                    ps.setLong(1, pokemonData.getId());
                    ps.setLong(2, move.getId());
                    ps.addBatch(); // Agregar al batch para optimización
                }

                ps.executeBatch(); // Ejecutar inserciones en lote
            }
        });

        return pokemonData;
    }

    @Transactional
    @Modifying
    public PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json) {
        
        List<String> abilityNameList = new ArrayList<>();
        for (JsonNode ability_json : pokemon_json.get("abilities")) {
            abilityNameList.add(ability_json.at("/ability/name").asText());
        }

        Set<AbilityData> definitiveAbilitySet = abilityData_Service.getAblitySetFromStringList(abilityNameList);

        String sqlQuery = "INSERT INTO `pokemon_data-ability_data` (pokemon_data_id, ability_data_id)"
            + "VALUES (?,?)";

        entityManager.unwrap(Session.class).doWork(connection -> {
            try(PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
                for (AbilityData ability : definitiveAbilitySet) {
                    ps.setLong(1, pokemonData.getId());
                    ps.setLong(2, ability.getId());

                    ps.addBatch();
                }

                ps.executeBatch();
            }
        });

        return pokemonData;
    }
    
    @Transactional
    @Modifying
    public PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json) {
        List<String> typeListString = new ArrayList<>();
        for (JsonNode current_type : pokemon_json.get("types")) {
            typeListString.add(current_type.at("/type/name").asText().toUpperCase());
        }

        String sql = "INSERT INTO pokemon_data_pokemon_type (pokemon_data_id, type_list) VALUES (?, ?)";

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (String type : typeListString) {
                    statement.setLong(1, pokemonData.getId());
                    statement.setString(2, type);
                    statement.executeUpdate();
                }
            }
    
        });
        return pokemonData;
    }

    @Transactional
    @Modifying
    public Set<PokemonData> assignPokemonAvailableInSV() {
        String sqlQuery = "UPDATE pokemon_data SET available_in_sv = true WHERE NAME IN (:names)";
        List<String> availablePokemons = csvFileReader.readFile("csvFiles/avaliableInSv.csv");

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("names", availablePokemons);
        query.executeUpdate();

        String sqlQueryUnavailable = "UPDATE pokemon_data SET available_in_sv = false WHERE NAME NOT IN (:names)";
        Query querySvUnavailable = entityManager.createNativeQuery(sqlQueryUnavailable);
        querySvUnavailable.setParameter("names", availablePokemons);
        querySvUnavailable.executeUpdate();

        return repo.getPokemonAvaliableInSV();
    }
}
