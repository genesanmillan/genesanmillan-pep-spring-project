package com.example.exception;

public class UnauthorizedException extends Exception {
    /**
     * Custom exception for Unauthorized exception 
     * @param message String to be relayed to Exception class and inserted into the response body
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
