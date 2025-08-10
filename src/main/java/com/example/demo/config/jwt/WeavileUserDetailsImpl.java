package com.example.demo.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.domain.user.WeavileUser;
import com.example.demo.repositories.WeavileUser_Repository;

@Component
public class WeavileUserDetailsImpl implements UserDetailsService{
    
    @Autowired
    WeavileUser_Repository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WeavileUser weavileUser = repo.findByUsername(username);

        if (weavileUser != null) {
            return User
                .withUsername(weavileUser.getUsername())
                .password(weavileUser.getPassword())
                .build();
        }

        else throw new UsernameNotFoundException("WeavileUser " + username + " not found");
    }


}
