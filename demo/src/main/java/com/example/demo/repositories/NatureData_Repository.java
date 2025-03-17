package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.team.NatureData;

public interface NatureData_Repository extends JpaRepository<NatureData, Long>{
    
    @Query( value = "SELECT * FROM nature_data", nativeQuery = true)
    Set<NatureData> getAllNatures();
}
