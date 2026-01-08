package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.team.NatureData;
import com.example.demo.dto.NatureDto;

public interface NatureDataRepository extends JpaRepository<NatureData, Long> {

    @Query(value = "SELECT * FROM nature_data", nativeQuery = true)
    Set<NatureData> getAllNatures();

    @Query("SELECT new com.example.demo.dto.NatureDto(na.name, na.decreased_stat, na.increased_stat) FROM NatureData na")
    Set<NatureDto> getAllNaturesAsDto();

    Optional<NatureData> findByName(String name);
}
