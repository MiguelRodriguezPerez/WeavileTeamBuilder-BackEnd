package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ItemData;


@Repository
public interface ItemData_Repository extends JpaRepository<ItemData,Long>{
    ItemData findByName(String name);
}
