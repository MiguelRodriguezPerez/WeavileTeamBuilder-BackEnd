package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ItemData;
import com.example.demo.domain.dto.ItemDataDto;


@Repository
public interface ItemData_Repository extends JpaRepository<ItemData, Long>{
    ItemData findByName(String name);

    @Query("SELECT new com.example.demo.domain.dto.ItemDataDto(i.image_sprite, i.name, i.description) FROM ItemData i")
    Set<ItemDataDto> findAllItemDataDto();
    
}
