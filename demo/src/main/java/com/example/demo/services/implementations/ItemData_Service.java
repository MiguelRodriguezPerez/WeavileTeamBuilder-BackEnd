package com.example.demo.services.implementations;

import java.sql.PreparedStatement;
import java.util.Set;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApiRequestManager;
import com.example.demo.config.ImageDownloader;
import com.example.demo.domain.ItemData;
import com.example.demo.repositories.ItemData_Repository;
import com.example.demo.services.interfaces.ItemData_Interface;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ItemData_Service implements ItemData_Interface {

    @Autowired
    ItemData_Repository repo;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ItemData saveItemData(ItemData itemData) {
        return repo.save(itemData);
    }

    @Override
    public ItemData getItemById(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteAllItemData() {
        repo.deleteAll();
    }

    @Override
    public ItemData getItemByName(String name) {
        return repo.findByName(name);
    }

    @Override
    @Transactional
    public ItemData requestItemToPokeApi(int number) {

        ItemData resultado = new ItemData();
        JsonNode item_source = ApiRequestManager.callGetRequest("https://pokeapi.co/api/v2/item/" + number);

        if (item_source != null) {

            resultado.setName(item_source.get("name").asText());

            if (item_source.get("effect_entries").isEmpty()) {
                for (JsonNode flavor_text_entry : item_source.get("flavor_text_entries")) {
                    if (flavor_text_entry.at("/language/name").asText().equals("en")) {
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
            // Algunas imágenes no tienen url para la imagen
            if (item_source.at("/sprites/default").asText().equals("null"))
                return null;
            else
                resultado.setImage_sprite(
                    ImageDownloader.getImage(
                        item_source.at("/sprites/default").asText()
                    )
                );

            return resultado;
        }

        else
            return null;
    }

    @Override
    @Transactional
    @Modifying
    public boolean requestAllItems() {
        final int total_items = 1203;
        String sql = "INSERT INTO item_data (name, description, image_sprite) VALUES (?, ?, ?)";

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (int i = 2; i <= total_items; i++) {
                    System.out.println("Objeto actual: " + i);

                    ItemData item = this.requestItemToPokeApi(i);
                    if (item != null) {
                        ps.setString(1, item.getName());
                        ps.setString(2, item.getDescription());
                        ps.setBytes(3, item.getImage_sprite()); // Insertar BLOB
                        ps.addBatch(); // Agregar al batch
                    }
                }
                ps.executeBatch(); // Ejecutar todas las inserciones en lote
            }
        });

        return true;
    }


    public Set<ItemData> getAllItems() {
        return repo.findAllItemData();
    }


}
