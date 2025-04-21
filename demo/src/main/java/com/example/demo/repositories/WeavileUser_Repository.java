package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.WeavileUser;


@Repository
public interface WeavileUser_Repository extends JpaRepository<WeavileUser,Long> {
    
    WeavileUser findByUsername(String username);
}
