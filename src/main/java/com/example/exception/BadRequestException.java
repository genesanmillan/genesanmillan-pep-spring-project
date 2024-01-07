package com.example.exception;

public class BadRequestException extends Exception {
    /**
     * Custom exception for Bad Request exceptions
     * @param message String to be relayed to Exception class and inserted into the response body
     */
    public BadRequestException(String message) {
        super(message);
    }
}
