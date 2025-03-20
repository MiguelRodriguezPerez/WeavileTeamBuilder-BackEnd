package com.example.demo.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.WeavileUser;
import com.example.demo.domain.user.WeavileUserDto;
import com.example.demo.repositories.WeavileUser_Repository;

import jakarta.servlet.http.Cookie;

@Service
public class AuthenticationService {

    @Autowired
    WeavileUser_Repository repo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    public WeavileUser authenticate(WeavileUserDto userRequest) {
        WeavileUser weavileUser = repo.findByUsername(userRequest.getUsername());
        if(weavileUser == null) throw new RuntimeException("AAAAAAAAAAA");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()));

        return weavileUser;
    }

    public Cookie generateCookieToken(WeavileUser usuario) {
        String token = jwtService.generateToken(usuario);
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");

        return cookie;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}

