package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.AbilityData;


@Repository
public interface AbilityData_Repository extends JpaRepository<AbilityData, Long> {

    AbilityData findByName(String name);

}
