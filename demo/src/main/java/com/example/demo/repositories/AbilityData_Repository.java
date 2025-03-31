package com.example.demo.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.movements.MoveData;


@Repository
public interface AbilityData_Repository extends JpaRepository<AbilityData, Long> {

    AbilityData findByName(String name);

    @Query(value = "SELECT * FROM ability_data", nativeQuery = true)
    Set<AbilityData> getAllAbilityData();

    @Transactional
    @Query(value = "SELECT * FROM ability_data WHERE name IN :names", nativeQuery = true)
    Set<AbilityData> getAblitySetFromStringList(@Param("names") List<String> names);
}
