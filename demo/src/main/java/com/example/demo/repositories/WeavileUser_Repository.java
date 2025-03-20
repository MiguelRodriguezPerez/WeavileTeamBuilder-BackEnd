package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.user.WeavileUser;


public interface WeavileUser_Repository extends JpaRepository<WeavileUser,Long> {
    
    WeavileUser findByUsername(String username);
}
