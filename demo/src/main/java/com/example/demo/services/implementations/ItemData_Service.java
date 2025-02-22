package com.example.demo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.domain.ItemData;
import com.example.demo.repositories.ItemData_Repository;
import com.example.demo.services.interfaces.ItemData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ItemData_Service implements ItemData_Interface {

    @Autowired
    ItemData_Repository repo;

    @Override
    public ItemData saveItemData(ItemData itemData) {
        return repo.save(itemData);
    }

    @Override
    public void deleteAllItemData() {
        repo.deleteAll();
    }

    @Override
    public ItemData findItemByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public ItemData requestItemToPokeApi(int number) {
       
        ItemData resultado = new ItemData();
        JsonNode item_source = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/item/" + number);

        resultado.setName(item_source.get("name").asText());
        
        if(item_source.get("effect_entries").isEmpty()) {
            for(JsonNode flavor_text_entry: item_source.get("flavor_text_entries")) {
                if(flavor_text_entry.get("language").get("name").asText().equals("en")) {
                    resultado.setDescription(flavor_text_entry.get("text").asText());
                }
            }
        }
        else {
            resultado.setDescription(item_source.get("effect_entries")
            .get(0)
            .get("short_effect").asText());
        }

        /* Abstraido al constructor a través de ImageDownloader */
        resultado.setImage_sprite(item_source.get("sprites").get("default").asText());

        return resultado;
    }

    @Override
    public boolean requestAllItems() {
        
        final int total_items = 1203;

        for(int i = 1; i < 4; i++) {
            System.out.println("Objeto actual: " + i);
            this.saveItemData(this.requestItemToPokeApi(i));
        }

        return true;
    }
    
}
