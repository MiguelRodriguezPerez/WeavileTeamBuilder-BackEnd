package com.example.demo.services.implementations;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.abilities.AbilityDTO;
import com.example.demo.domain.abilities.AbilityData;
import com.example.demo.repositories.AbilityDataRepository;
import com.example.demo.services.interfaces.AbilityInterface;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AbilityService implements AbilityInterface {

    @Autowired
    AbilityDataRepository repo;

    @Override
    public AbilityData saveAbility(AbilityData ability) {
        return repo.save(ability);
    }

    @Override
    public AbilityData findAbilityByName(String name){
        return repo.findByName(name);
    }

    @Override
    public AbilityData requestAbilityToPokeApi(int num) {
        AbilityData resultado = new AbilityData();

        /*TODO: Si short_effect existe, pasarlo como description. 
        Si no, usa el flavor de antes
         */

        try {
            URL url = new URI("https://pokeapi.co/api/v2/ability/" + num).toURL();
            HttpURLConnection solicitud = (HttpURLConnection) url.openConnection();
            solicitud.setRequestMethod("GET");

            if (solicitud.getResponseCode() == 200) {
                /* Resultado petición JSON a String */
                String resultadoPeticion = new String(url.openStream().readAllBytes());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonSource = objectMapper.readTree(resultadoPeticion);

                resultado.setName(jsonSource.get("name").asText());

                JsonNode arrayEntradas = jsonSource.get("flavor_text_entries");

                for (JsonNode entry : arrayEntradas) {
                    if (entry.get("language").get("name").asText().equals("en")) {
                        resultado.setDescription(entry.get("flavor_text").asText());
                        break;
                    }
                }

                return resultado;
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    

    public boolean requestSeveralAbilities(int number_start_ability, int number_end_ability) {

        if(number_end_ability <= number_start_ability) 
            throw new RuntimeException("Params are equal or number_end_ability is greater than number_start_ability");

        for(int i = number_start_ability; i < number_end_ability; i++) {

            System.out.println("AAAAAAAAAAAAAAAAA");

            AbilityData currentAbility = this.requestAbilityToPokeApi(i);
            if(this.findAbilityByName(currentAbility.getName()) == null) this.saveAbility(currentAbility);

        }

        return true;
    }
}
