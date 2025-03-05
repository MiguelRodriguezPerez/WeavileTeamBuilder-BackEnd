package com.example.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.movements.MoveData;

@Repository
public interface MoveData_Repository extends JpaRepository<MoveData, Long> {

    @Query(value = "SELECT * FROM move_data", nativeQuery = true)
    Set<MoveData> getAllMoveData();

    MoveData findByName(String name);
}
