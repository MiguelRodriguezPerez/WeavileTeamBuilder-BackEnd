package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.movements.MoveData;

@Repository
public interface MoveData_Repository extends JpaRepository<MoveData,Long> {
    
}
