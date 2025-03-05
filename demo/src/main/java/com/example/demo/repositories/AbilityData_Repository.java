package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.AbilityData;


@Repository
public interface AbilityData_Repository extends JpaRepository<AbilityData, Long> {

    AbilityData findByName(String name);

    @Query(value = "SELECT * FROM ability_data", nativeQuery = true)
    Set<AbilityData> getAllAbilityData();
}
