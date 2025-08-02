package com.example.demo.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.movements.MoveData;

@Repository
public interface MoveDataRepository extends JpaRepository<MoveData, Long> {

    @Query(value = "SELECT * FROM move_data", nativeQuery = true)
    Set<MoveData> getAllMoveData();

    MoveData findByName(String name);

    @Transactional
    @Query(value = "SELECT * FROM move_data WHERE name IN :names", nativeQuery = true)
    Set<MoveData> getMoveDataSetFromStringList(@Param("names") List<String> names);
}
