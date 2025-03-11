package com.example.demo.services.implementations;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.NatureData;
import com.example.demo.repositories.NatureData_Repository;
import com.example.demo.services.interfaces.NatureData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class NatureData_Service implements NatureData_Interface {

    @Autowired
    NatureData_Repository repo;

    @Override
    public NatureData saveNatureData(NatureData natureData) {
        return repo.save(natureData);
    }

    @Override
    public NatureData getNatureDataById(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteAllNatureData() {
        repo.deleteAll();
    }

    @Override
    public NatureData requestNatureToPokeApi(int number) {
        NatureData natureData = new NatureData();
        JsonNode nature_json = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/nature/" + number);

        natureData.setName(nature_json.at("/name").asText());

        if(nature_json.at("/increased_stat") != null) 
            natureData.setIncreased_stat(nature_json.at("/increased_stat/name").asText());
        
        if(nature_json.at("/decreased_stat") != null) 
            natureData.setDecreased_stat(nature_json.at("/decreased_stat/name").asText());

        return natureData;
    }

    @Override
    public boolean requestAllNatures() {
        final int nature_number = 25;

        for (int i = 1; i < nature_number; i++) {
            System.out.println("Naturaleza " + i);
            this.saveNatureData(this.requestNatureToPokeApi(i));
        }

        return true;
    }

    @Override
    public Set<NatureData> getAllNatures() {
        return repo.getAllNatures();
    }
    
}
