package com.example.project.exeptions;

public class UserAlreadyExists extends RuntimeException {
    private final String message;

    public UserAlreadyExists(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Registration problem! We already have user with login: " + message;
    }
}
