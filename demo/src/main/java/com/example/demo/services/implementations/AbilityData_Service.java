package com.example.demo.services.implementations;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.AbilityData;
import com.example.demo.repositories.AbilityData_Repository;
import com.example.demo.services.interfaces.AbilityData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class AbilityData_Service implements AbilityData_Interface {

    @Autowired
    AbilityData_Repository repo;

    @Override
    public AbilityData saveAbility(AbilityData ability) {
        return repo.save(ability);
    }

    @Override
    public void deleteAllAbilities() {
        repo.deleteAll();
    }

    @Override
    public AbilityData getAbilityByName(String name) {
        return repo.findByName(name);
    }

    /*
     * Este método realiza la petición de una habilidad pokemón a pokeapi.
     * Recibe un json que es un "stream de bytes".
     * Coges esos bytes y lo insertas en un "new String(bytes)" para recibir un
     * string del json.
     * 
     * Mediante el uso de ObjectMapper y JsonNode accedes a los campos del json
     * convertido a string
     */

    @Override
    public AbilityData requestAbilityToPokeApi(int num) {

        AbilityData resultado = new AbilityData();
        JsonNode jsonSource = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/ability/" + num);

        // Extrae el atributo "name" del json y lo almacena en la nueva entidad
        resultado.setName(jsonSource.get("name").asText());

        /*
         * effect_entries es un valor que contiene el efecto de la habilidad bien
         * descrito (con su efecto
         * competitivo). El problema es que no esta presente en todos los json de las
         * habilidades, por lo que
         * tienes que evaluar si ese campo existe, y si no existe, tienes que
         * seleccionar la descripción de
         * "flavor_text_entries"
         */

        /*
         * Ambos campos estan presentes en varios idiomas,
         * por eso se comprueba que "language" equivalga a "en"
         */

        if (!jsonSource.get("effect_entries").isEmpty()) {
            for (JsonNode effect_entry : jsonSource.get("effect_entries")) {
                if (effect_entry.at("/language/name").asText().equals("en"))
                    resultado.setDescription(effect_entry.get("short_effect").asText());
            }
        }

        else {
            for (JsonNode flavor_entry : jsonSource.get("flavor_text_entries")) {
                if (flavor_entry.at("/language/name").asText().equals("en")) {
                    resultado.setDescription(flavor_entry.get("flavor_text").asText());
                    // Algunas veces devuelve varias veces la misma respuesta, así que detengo el
                    // bucle
                    break;
                }
            }
        }

        return resultado;
    }

    @Override
    public boolean requestAllAbilitiesToApi() {

        final int numero_habilidades = 307;

        for (int i = 1; i <= numero_habilidades; i++) {
            AbilityData ab = this.requestAbilityToPokeApi(i);
            if (ab != null)
                this.saveAbility(ab);
            else
                throw new RuntimeException("Error al recibir la habilidad numero " + i);
        }

        return true;
    }

    @Override
    public Set<AbilityData> getAllAbilityData() {
        return repo.getAllAbilityData();
    }
}
