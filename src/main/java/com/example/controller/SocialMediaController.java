package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.entity.Account;
import com.example.exception.InvalidLoginException;
import com.example.exception.RegisterException;
import com.example.exception.UsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RestControllerAdvice
public class SocialMediaController {
    // Services
    AccountService accountService;
    MessageService messageService;

    /**
     * Autowired services for injection
     * @param accountService AccountService to be used
     * @param messageService MessageService to be used
     */
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * User Registration - For endpoint POST localhost:8080/register. It registers the Account provided in the RequestBody
     * if data conforms to the following constraints:
     * - Username not blank
     * - Username doesn't already exist in the database
     * - Password is atleast four characters
     * @param account Account with the data to register
     * @return An OK HTTP Status with the Account object as body (with id)
     * @throws RegisterException Returns 400 HTTP Status if it fails the constraints
     * @throws UsernameException Returns 409 HTTP Status if username is not available
     */
    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) throws RegisterException, UsernameException {
        if(!accountService.checkConstraints(account)) {
            throw new RegisterException("Username must not be blank and password must be at least 4 characters long");
        }

        if(!accountService.usernameAvailable(account.getUsername())) {
            throw new UsernameException("Username not available");
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountService.register(account));
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account accountToCheck) throws InvalidLoginException {
        Account account = accountService.getAccountByUsername(accountToCheck.getUsername());

        if(account != null) {
            if(account.getPassword().equals(accountToCheck.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(account);
            }
        }
        
        throw new InvalidLoginException("Unsuccessful login.");
    }

}
