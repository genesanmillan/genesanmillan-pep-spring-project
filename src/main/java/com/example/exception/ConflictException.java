package com.example.exception;

public class ConflictException extends Exception {
    /**
     * Custom exception for Conflict exceptions
     * @param message String to be relayed to Exception class and inserted into the response body
     */
    public ConflictException(String message) {
        super(message);
    }
}
