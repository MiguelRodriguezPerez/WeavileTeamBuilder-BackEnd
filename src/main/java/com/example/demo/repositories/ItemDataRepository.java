package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ItemData;
import com.example.demo.dto.ItemDto;

@Repository
public interface ItemDataRepository extends JpaRepository<ItemData, Long> {
    Optional<ItemData> findByName(String name);

    @Query("SELECT new com.example.demo.dto.ItemDto(idata.image_sprite, idata.name, idata.description) FROM ItemData idata")
    Set<ItemDto> findAllItemDataDto();

}
