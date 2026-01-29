package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.config.CsvFileReader;
import com.example.demo.config.ImageDownloader;
import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.dto.pokemon.AbilityDto;
import com.example.demo.dto.pokemon.MissignoDto;
import com.example.demo.dto.pokemon.PokemonDataDto;
import com.example.demo.dto.pokemon.PokemonTypeDto;
import com.example.demo.repositories.PokemonDataRepository;
import com.example.demo.services.interfaces.PokemonDataInterface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PokemonDataService implements PokemonDataInterface {

    @Autowired
    PokemonDataRepository repo;

    @Autowired
    AbilityDataService abilityDataService;

    @Autowired
    MoveDataService moveDataService;

    @Autowired
    PokemonTypeService pokemonTypeService;

    @Autowired
    DataSource dataSource;

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


    @Override
    public PokemonDataDto getPokemonDataById (Long id) {
        PokemonDataDto resultado = PokemonDataDto.builder().build();

        String pokemonDataQuery = """
                SELECT id, available_in_sv, base_hp, base_attack, base_defense,
                       base_special_attack, base_special_defense, base_speed,
                       front_default_sprite, pc_sprite,
                       name
                FROM pokemon_data
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(pokemonDataQuery);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resultado = PokemonDataDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .base_hp(rs.getInt("base_hp"))
                    .base_attack(rs.getInt("base_attack"))
                    .base_defense(rs.getInt("base_defense"))
                    .base_special_attack(rs.getInt("base_special_attack"))
                    .base_special_defense(rs.getInt("base_special_defense"))
                    .base_speed(rs.getInt("base_speed"))
                    .front_default_sprite(rs.getBytes("front_default_sprite"))
                    .pc_sprite(rs.getBytes("pc_sprite"))
                    .build();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        resultado.setMove_list(moveDataService.getPokemonMovesByPokemonId(id));
        resultado.setAbility_list(abilityDataService.getAbilitiesByPokemonId(id));
        resultado.setType_list(pokemonTypeService.getPokemonTypesByPokemonDataId(id));

        return resultado;
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

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(firstQuery,
                    Statement.RETURN_GENERATED_KEYS);

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
        availablePokemons.replaceAll(String::toLowerCase);

        String updateAvailableSql = "UPDATE pokemon_data SET available_in_sv = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateAvailableSql)) {
            boolean available = availablePokemons.contains(pokemonData.getName().toLowerCase());

            preparedStatement.setBoolean(1, available);
            preparedStatement.setLong(2, pokemonData.getId()); // para el WHERE

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* No tengo ni idea de si este engendro es mejor que separar tres subconsultas y juntarlo todo.
    Preguntale a un desarrollador serio cuando te cuadre */

    /* Esta consulta es horriblemente complicada así que presta atención a los comentarios */
    @Override
    public Set<MissignoDto> getAllPokemonForMissignoGrid() {

        /* En esta consulta te van a llegar varias filas del mismo pokemón con el mismo id. De alguna manera
        necesitas evitar filas duplicadas. 
        
        Un map te permite asignar un MissignoDto a un id key, impidiendo filas/ids duplicados*/
        Map<Long,MissignoDto> mapQuery = new HashMap<>();
        Set<MissignoDto> resultado = new HashSet<>();
        /* Esta consulta es un LEFT JOIN y no un INNER JOIN porque va a haber celdas en las que el tipo o la 
        habilidad sean nulos. Si te viniera una celda nula con INNER JOIN, la consulta fallaría. 
        
        Aún así es buena práctica hacer un LEFT JOIN aunque no esperes datos duplicados.*/
        String query = """
                SELECT
                    p.id                AS pokemon_id,
                    p.name              AS pokemon_name,
                    p.base_hp           AS base_hp,
                    p.base_attack       AS base_attack,
                    p.base_defense      AS base_defense,
                    p.base_special_attack AS base_special_attack,
                    p.base_special_defense AS base_special_defense,
                    p.base_speed        AS base_speed,
                    p.pc_sprite         AS pc_sprite,
                    ad.id AS ability_id,
                    ad.name AS ability_name,
                    pt.id AS pokemon_type_id,
                    pt.name AS pokemon_type_name,
                    pt.sprite AS pokemon_type_sprite

                FROM pokemon_data p 
            """
        +
            "LEFT JOIN pokemon_data_ability_data pad ON pad.pokemon_data_id = p.id " +
            "LEFT JOIN ability_data ad ON ad.id = pad.ability_data_id " +
            "LEFT JOIN pokemon_data_pokemon_type pdpt ON pdpt.pokemon_data_id = p.id " +
            "LEFT JOIN pokemon_type pt ON pt.id = pdpt.pokemon_type_id ";


        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                /* Lo primero que haces es obtener el id del pokemón para saber si te viene duplicado o no */
                Long id = rs.getLong("pokemon_id");

                /* mapQuery.computeIfAbsent recibe el id y en base a el, decide si ya has procesado ese pokemón o no. 
                Si no lo has procesado, creará una nueva entidad y guardará en ella su id, nombre y stats
                
                No me preguntes cuando han pasado los resultados de la consulta al map.*/
                MissignoDto currentPokemon = mapQuery.computeIfAbsent(id, k -> {
                    try {
                        return MissignoDto.builder()
                                .id(id)
                                .name(rs.getString("pokemon_name"))
                                .base_hp(rs.getInt("base_hp"))
                                .base_attack(rs.getInt("base_attack"))
                                .base_defense(rs.getInt("base_defense"))
                                .base_special_attack(rs.getInt("base_special_attack"))
                                .base_special_defense(rs.getInt("base_special_defense"))
                                .base_speed(rs.getInt("base_speed"))
                                .pc_sprite(rs.getBytes("pc_sprite"))
                                .type_list(new HashSet<>())    // Inicializamos las listas
                                .ability_list(new HashSet<>())
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                /* Si el id de la habilidad que obtienes en la consulta no es nulo, lo usarás para crear
                una nueva entidad */
                if (rs.getString("ability_id") != null){
                    AbilityDto currentAbility = AbilityDto.builder()
                            .id(Long.parseLong(rs.getString("ability_id")))
                            .name(rs.getString("ability_name"))
                            // No necesitas la descripción en MissignoCard
                            .description("")
                            .build();

                    /* Ahora tienes que averiguar si la entidad construida ya existe en la colección de habilidades
                    del pokemón que ya estás procesando. Si no existe, la añades.
                    
                    La comparación se realiza en base al id de la habilidad consultada */
                    if (!currentPokemon.getAbility_list().contains(currentAbility)) 
                        currentPokemon.getAbility_list().add(currentAbility);
                }


                // Misma lógica que en la habilidad del pokemón
                if (rs.getString("pokemon_type_id") != null) {
                    PokemonTypeDto currentType = PokemonTypeDto.builder()
                                .id(Long.parseLong(rs.getString("pokemon_type_id")))
                                .name(rs.getString("pokemon_type_name"))
                                .sprite(rs.getBytes("pokemon_type_sprite"))
                                .build();
                    if (!currentPokemon.getType_list().contains(currentType)) {
                        currentPokemon.getType_list().add(currentType);
                    }
                }
                
                // Añades el nuevo pokemón a la lista
                resultado.add(currentPokemon);
            };
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultado;
    }

}
