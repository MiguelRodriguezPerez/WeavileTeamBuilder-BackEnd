package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.team.NatureData;

public interface NatureDataRepository extends JpaRepository<NatureData, Long> {



    Optional<NatureData> findByName(String name);
}
