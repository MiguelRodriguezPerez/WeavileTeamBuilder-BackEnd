package com.example.demo.services.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.team.NatureData;
import com.example.demo.dto.NatureDto;
import com.example.demo.exceptions.NatureNotFoundException;
import com.example.demo.repositories.NatureDataRepository;
import com.example.demo.services.interfaces.NatureDataInterface;
import com.fasterxml.jackson.databind.JsonNode;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class NatureDataService implements NatureDataInterface {

    @Autowired
    NatureDataRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    @Override
    public NatureData getNatureDataById(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteAllNatureData() {
        repo.deleteAll();
    }

    @Override
    @Transactional
    public NatureData requestNatureToPokeApi(int number) {
        NatureData natureData = new NatureData();
        JsonNode nature_json = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/nature/" + number);

        natureData.setName(nature_json.at("/name").asText());

        if (nature_json.at("/increased_stat") != null)
            natureData.setIncreased_stat(nature_json.at("/increased_stat/name").asText());

        if (nature_json.at("/decreased_stat") != null)
            natureData.setDecreased_stat(nature_json.at("/decreased_stat/name").asText());

        return natureData;
    }

    @Override
    @Transactional
    @Modifying
    public boolean requestAllNatures() {
        String sqlQuery = "INSERT INTO nature_data (id,name, increased_stat, decreased_stat) VALUES (?, ? ,? ,?)";
        final int nature_number = 25;

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

                for (int i = 1; i <= nature_number; i++) {
                    System.out.println("Naturaleza " + i);

                    NatureData natureData = this.requestNatureToPokeApi(i);
                    ps.setInt(1, i);
                    ps.setString(2, natureData.getName());
                    ps.setString(3, natureData.getIncreased_stat());
                    ps.setString(4, natureData.getDecreased_stat());

                    ps.addBatch();
                }

                ps.executeBatch();
            }
        });

        return true;
    }

    @Override
    public Set<NatureDto> getAllNatures() {
        Set<NatureDto> resultado = new HashSet<>();
        String query = """
                SELECT id, decreased_stat, increased_stat, name
                FROM nature_data
                """;
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resultado.add(
                    NatureDto.builder()
                        .id(rs.getLong("id"))
                        .decreased_stat(rs.getString("decreased_stat"))
                        .increased_stat(rs.getString("increased_stat"))
                        .name(rs.getString("name"))
                        .build()
                );
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultado;

    }

    @Override
    public NatureData getNatureByName(String name) {
        return repo.findByName(name).orElseThrow(() -> new NatureNotFoundException(name));
    }

    @Override
    public NatureDto convertNatureDataToDto(NatureData data) {
        return NatureDto.builder()
                .name(data.getName())
                .decreased_stat(data.getDecreased_stat())
                .increased_stat(data.getIncreased_stat())
                .build();
    }


    

}
