package com.example.exception;

public class UsernameException extends Exception {
    /**
     * Custom exception for username exceptions
     * @param message String to be relayed to Exception class
     */
    public UsernameException(String message) {
        super(message);
    }
}
