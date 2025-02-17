package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.abilities.AbilityData;
import java.util.List;


@Repository
public interface AbilityDataRepository extends JpaRepository<AbilityData, Long> {

    AbilityData findByName(String name);

}
