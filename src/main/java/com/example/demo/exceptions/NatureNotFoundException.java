package com.example.demo.exceptions;

public class NatureNotFoundException extends RuntimeException {
    public NatureNotFoundException(String name) {
        super("Nature not found: " + name);
    }
}
