package com.example.demo.config.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
        FilterChain filterChain) throws ServletException, IOException { 
        /*Esta parte es solo para obtener el token de la cookie httpOnly jwtToken */
        String jwtToken = null; 
        Cookie[] cookies = request.getCookies(); 
        if (cookies != null) { 
            for (Cookie cookie : cookies) { 
                if ("jwtToken".equals(cookie.getName())) { 
                    jwtToken = cookie.getValue(); 
                    break; 
                } 
            } 
        } 
        
        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.extractUsername(jwtToken); 
                if (username != null) { 
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
                    if (jwtService.isTokenValid(jwtToken, userDetails)) { 
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
                        SecurityContextHolder.getContext().setAuthentication(authToken); 
                    } 
                } 
            } 
            filterChain.doFilter(request, response); 
        }
    }


