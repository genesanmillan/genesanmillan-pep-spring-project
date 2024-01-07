package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.exception.ConflictException;
import com.example.exception.UnauthorizedException;
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
    public ResponseEntity<Account> register(@RequestBody Account account) throws BadRequestException, ConflictException {
        if(!accountService.constraintsPassed(account)) {
            throw new BadRequestException("Username must not be blank and password must be at least 4 characters long");
        }

        if(!accountService.usernameAvailable(account.getUsername())) {
            throw new ConflictException("Username not available");
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountService.register(account));
    }

    /**
     * Login - For endpoint POST localhost:8080/login. It checks the provided Account's username and see if it matches
     * the password from the database's Account with the same username
     * @param accountToCheck Account to be checked from the POST's request body
     * @return An OK HTTP Status with the Account object as body (with id)
     * @throws UnauthorizedException Returns 401 HTTP Status if login failed
     */
    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account accountToCheck) throws UnauthorizedException {
        Account account = accountService.getAccountByUsername(accountToCheck.getUsername());

        if(account != null) {
            if(account.getPassword().equals(accountToCheck.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(account);
            }
        }
        
        throw new UnauthorizedException("Unsuccessful login");
    }

    /**
     * Create New Message - For endpoint POST localhost:8080/messages. It creates the Message provided in the RequestBody
     * if data conforms to the following constraints:
     * - message_text is not blank
     * - message_text is under 255 characters
     * - posted_by (account_id) exists in the database
     * @param message Message object with the data to post
     * @return An OK HTTP Status with the Message object as body (with id)
     * @throws BadRequestException Returns 400 HTTP Status if it fails the constraints
     */
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws BadRequestException {
        if(!accountService.usernameAvailable(message.getPosted_by()) &&
            messageService.constraintsPassed(message.getMessage_text())) {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.createMessage(message));
        }

        throw new BadRequestException("Message creation not successful");
    }

    /**
     * Get All Messages - For endpoint GET localhost:8080/messages. Gets all the messages from the database
     * @return An OK HTTP Status with the list of all the messages as body
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessages());
    }

    /**
     * Get One Message Given Message Id - For endpoint GET localhost:8080/messages/{message_id} 
     * Get a Message object with the specific ID
     * @param message_id ID to use for retrieval
     * @return An OK HTTP Status with the Message object as body (or empty body if no message available)
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessage(@PathVariable int message_id) {
        Message message = messageService.getMessage(message_id);
        return (message != null) ? 
            ResponseEntity.status(HttpStatus.OK).body(messageService.getMessage(message_id)) :
            ResponseEntity.status(HttpStatus.OK).build();
    }

    /** 
     * Delete a Message Given Message ID - For endpoint DELETE localhost:8080/messages/{message_id} 
     * Delete a message with the given message_id from the database
     * @param message_id Integer to be utilized
     * @return Response body with number of rows updated (1) if Message existed and deleted, empty if it didn't exist
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int message_id) {
        return (messageService.deleteMessage(message_id)) ? 
            ResponseEntity.status(HttpStatus.OK).body(1) :
            ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update Message Given Message ID - For endpoint PATCH localhost:8080/messages/{message_id}. 
     * It updates message_text of the Message object with the given message_id
     * if data conforms to the following constraints:
     * - message_id exists in the database
     * - message_text is not blank
     * - message_text not over 255 characters
     * 
     * @param message_id Integer to find the Message
     * @param request_body Contains the string to replace the old message_text
     * @return Response body with number of rows updated (1) if successfully updated
     * @throws BadRequestException Returns 400 HTTP Status if it fails the constraints
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int message_id, @RequestBody Map<String, String> request_body) throws BadRequestException {
        if(messageService.updateMessage(message_id, request_body.get("message_text"))) {
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
        throw new BadRequestException("Update not successful");
    }

    /**
     * Get All Messages From User Given Account ID - For endpoint GET localhost:8080/accounts/{account_id}/messages
     * Finds all the messages from the given account_id user
     * @param posted_by Integer identifier for the messages
     * @return An OK HTTP Status with the list of all the messages from the account_id as body
     */
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable int account_id) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages(account_id));
    }
}
