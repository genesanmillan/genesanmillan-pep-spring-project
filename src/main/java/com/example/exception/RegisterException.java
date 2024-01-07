package com.example.exception;

public class RegisterException extends Exception {
    /**
     * Custom exception for register exceptions
     * @param message String to be relayed to Exception class
     */
    public RegisterException(String message) {
        super(message);
    }
}
