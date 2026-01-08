package com.example.demo.exceptions;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(String name) {
        super("Pokemon not found: " + name);
    }
    
}

