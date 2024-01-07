// Global Exceptions Handler to showcase Spring Framework utility

package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    /**
     * For handling Registration exception
     * @param ex Register Exception
     * @return 400 HTTP Status
     */
    @ExceptionHandler(RegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> registerFailed(RegisterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * For handling Username exception
     * @param ex Username Exception
     * @return 409 HTTP Status
     */
    @ExceptionHandler(UsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> usernameNotAvailable(UsernameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * For handling InvalidLogin exception
     * @param ex InvalidLogin Exception
     * @return 401 HTTP Status
     */
    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> loginException(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}