package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ItemData;


@Repository
public interface ItemData_Repository extends JpaRepository<ItemData, Long>{
    ItemData findByName(String name);

    @Query(value = "SELECT * FROM item_data", nativeQuery = true)
    Set<ItemData> findAllItemData();
}
