package com.example.exception;

public class InvalidLoginException extends Exception {
    /**
     * Custom exception for Invalid login exception
     * @param message String to be relayed to Exception class
     */
    public InvalidLoginException(String message) {
        super(message);
    }
}
