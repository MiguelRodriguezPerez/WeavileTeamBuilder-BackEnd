package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.config.CsvFileReader;
import com.example.demo.config.ImageDownloader;
import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.pokemon.MissignoGridDto;
import com.example.demo.dto.pokemon.PokemonDto;
import com.example.demo.repositories.PokemonDataRepository;
import com.example.demo.services.interfaces.PokemonDataInterface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PokemonDataService implements PokemonDataInterface {

    @Autowired
    PokemonDataRepository repo;

    @Autowired
    AbilityDataService abilityData_Service;

    @Autowired
    MoveDataService moveData_Service;

    @Autowired
    DataSource dataSource;

    @Autowired
    CsvFileReader csvFileReader;

    @Autowired
    Environment environment;

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
        // 15:06:13
        final int num_pokemon = 1025; // 1025

        // TODO: Adaptar a inserciones en batch

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
        PokemonData resultado = new PokemonData();

        resultado.setName(pokemon_json.get("name").asText());
        resultado = this.assignPokemonDataSprites(resultado, pokemon_json);
        resultado = this.assignPokemonDataStats(resultado, pokemon_json);

        String firstQuery = "INSERT INTO pokemon_data (name, base_hp, base_attack, base_defense"
                + ", base_special_attack, base_special_defense, base_speed"
                + ", front_default_sprite, pc_sprite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(firstQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, resultado.getName());
            preparedStatement.setInt(2, resultado.getBase_hp());
            preparedStatement.setInt(3, resultado.getBase_attack());
            preparedStatement.setInt(4, resultado.getBase_defense());
            preparedStatement.setInt(5, resultado.getBase_special_attack());
            preparedStatement.setInt(6, resultado.getBase_special_defense());
            preparedStatement.setInt(7, resultado.getBase_speed());
            preparedStatement.setBytes(8, resultado.getFront_default_sprite());
            preparedStatement.setBytes(9, resultado.getPc_sprite());

            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            resultado.setId(keys.getLong(1));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultado;
    }

    @Transactional
    public PokemonData createPokemonDataRelations(PokemonData pokemonData, JsonNode pokemon_json) {

        this.assignPokemonDataAbilities(pokemonData, pokemon_json);
        this.assignPokemonDataMoves(pokemonData, pokemon_json);
        this.assignPokemonDataTypes(pokemonData, pokemon_json);
        this.assignPokemonAvailableInSV(pokemonData);

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
        List<Long> moveIds = new ArrayList<>();

        for (JsonNode move_json : pokemon_json.at("/moves")) {
            moveNameList.add(move_json.at("/move/name").asText());
        }

        try (Connection connection = dataSource.getConnection()) {
            /* Toda esta tontería es para pasarle la colección como argumento a la query */
            String inClause = moveNameList.stream()
                    .map(name -> "?")
                    .collect(Collectors.joining(","));
            String selectMoveIds = "SELECT id FROM move_data WHERE name IN (" + inClause + ")";

            PreparedStatement psSelect = connection.prepareStatement(selectMoveIds);
            for (int i = 0; i < moveNameList.size(); i++) {
                psSelect.setString(i + 1, moveNameList.get(i));
            }

            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) {
                moveIds.add(rs.getLong("id"));
            }

            String insertRelation = "INSERT INTO pokemon_data_move_data (pokemon_data_id, move_data_id) VALUES (?, ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertRelation);

            for (Long moveId : moveIds) {
                psInsert.setLong(1, pokemonData.getId());
                psInsert.setLong(2, moveId);
                psInsert.addBatch();
            }

            psInsert.executeBatch();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return pokemonData;
    }

    @Transactional
    @Modifying
    public PokemonData assignPokemonDataAbilities(PokemonData pokemonData, JsonNode pokemon_json) {
        List<String> abilityNameList = new ArrayList<>();
        List<Long> abilityIds = new ArrayList<>();

        for (JsonNode ability_json : pokemon_json.get("abilities")) {
            abilityNameList.add(ability_json.at("/ability/name").asText());
        }

        try (Connection connection = dataSource.getConnection()) {
            // Construir IN dinámico
            String inClause = abilityNameList.stream()
                    .map(name -> "?")
                    .collect(Collectors.joining(","));
            String selectAbilityIds = "SELECT id FROM ability_data WHERE name IN (" + inClause + ")";

            // Seleccionar IDs de abilities
            PreparedStatement psSelect = connection.prepareStatement(selectAbilityIds);
            for (int i = 0; i < abilityNameList.size(); i++) {
                psSelect.setString(i + 1, abilityNameList.get(i));
            }

            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) {
                abilityIds.add(rs.getLong("id"));
            }

            // Insertar relación en tabla intermedia
            String insertRelation = "INSERT INTO pokemon_data_ability_data (pokemon_data_id, ability_data_id) VALUES (?, ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertRelation);

            for (Long abilityId : abilityIds) {
                psInsert.setLong(1, pokemonData.getId());
                psInsert.setLong(2, abilityId);
                psInsert.addBatch();
            }

            psInsert.executeBatch();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return pokemonData;
    }

    @Transactional
    @Modifying
    public PokemonData assignPokemonDataTypes(PokemonData pokemonData, JsonNode pokemon_json) {
        List<String> typeListString = new ArrayList<>();
        List<Long> typeIds = new ArrayList<>();

        for (JsonNode current_type : pokemon_json.get("types")) {
            typeListString.add(current_type.at("/type/name").asText().toLowerCase());
        }

        try (Connection connection = dataSource.getConnection()) {
            String questionMarks = typeListString.stream()
                    .map(t -> "?")
                    .collect(Collectors.joining(","));
            String selectTypeIds = "SELECT id FROM pokemon_type WHERE nombre IN (" + questionMarks + ")";

            PreparedStatement psSelect = connection.prepareStatement(selectTypeIds);
            for (int i = 0; i < typeListString.size(); i++) {
                psSelect.setString(i + 1, typeListString.get(i));
            }

            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) {
                typeIds.add(rs.getLong("id"));
            }

            String insertRelation = "INSERT INTO pokemon_data_pokemon_type (pokemon_data_id, pokemon_type_id) VALUES (?, ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertRelation);

            for (Long typeId : typeIds) {
                psInsert.setLong(1, pokemonData.getId());
                psInsert.setLong(2, typeId);
                psInsert.addBatch();
            }

            psInsert.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pokemonData;
    }

    @Transactional
    @Modifying
    public boolean assignPokemonAvailableInSV(PokemonData pokemonData) {
        List<String> availablePokemons = csvFileReader.readFile("csvFiles/avaliableInSv.csv");
        String updateAvailableSql = "UPDATE pokemon_data SET available_in_sv = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(updateAvailableSql);
            preparedStatement.setBoolean(1, availablePokemons.contains(pokemonData.getName()));
            
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public Set<PokemonData> getAllSVPokemon() {
        return repo.getPokemonAvaliableInSV();
    }

    public Set<MissignoGridDto> convertToMissignoGridDTO(Set<PokemonData> pokemonDataSet) {
        // Utilizando Java 8 Streams para la conversión de forma limpia
        return pokemonDataSet.stream()
                .map(pokemonData -> {
                    MissignoGridDto missignoGridDTO = new MissignoGridDto();
                    missignoGridDTO.setId(pokemonData.getId());
                    missignoGridDTO.setName(pokemonData.getName());
                    missignoGridDTO.setBase_hp(pokemonData.getBase_hp());
                    missignoGridDTO.setBase_attack(pokemonData.getBase_attack());
                    missignoGridDTO.setBase_defense(pokemonData.getBase_defense());
                    missignoGridDTO.setBase_special_attack(pokemonData.getBase_special_attack());
                    missignoGridDTO.setBase_special_defense(pokemonData.getBase_special_defense());
                    missignoGridDTO.setBase_speed(pokemonData.getBase_speed());
                    missignoGridDTO.setPc_sprite(pokemonData.getPc_sprite());

                    // Convertir las listas de tipo y habilidades a DTOs correspondientes (si es
                    // necesario)
                    missignoGridDTO.setType_list(pokemonData.getType_list());
                    missignoGridDTO.setAbility_list(
                            pokemonData.getAbility_list().stream()
                                    .map(ability -> {
                                        return abilityData_Service.convertAbilityEntityToDto(ability);
                                    })
                                    .collect(Collectors.toSet()));

                    return missignoGridDTO;
                })
                .collect(Collectors.toSet()); // Devuelve un Set<MissignoGridDTO>
    }

    @Override
    public PokemonDto convertPokemonDataToDto(PokemonData data) {
        return PokemonDto.builder()
                .name(data.getName())
                .front_default_sprite(data.getFront_default_sprite())
                .pc_sprite(data.getPc_sprite())
                .base_hp(data.getBase_hp())
                .base_attack(data.getBase_attack())
                .base_defense(data.getBase_defense())
                .base_special_attack(data.getBase_special_attack())
                .base_special_defense(data.getBase_special_defense())
                .base_speed(data.getBase_speed())
                .type_list(
                        data.getType_list()
                                .stream()
                                .map(PokemonType::getNombre)
                                .collect(Collectors.toSet()))
                .ability_list(
                        data.getAbility_list().stream()
                                .map(ability -> {
                                    return abilityData_Service.convertAbilityEntityToDto(ability);
                                }).collect(Collectors.toSet()))
                .move_list(
                        data.getMove_list().stream()
                                .map(move -> {
                                    return moveData_Service.convertMoveDataToDto(move);
                                }).collect(Collectors.toSet()))
                .build();
    }

}
