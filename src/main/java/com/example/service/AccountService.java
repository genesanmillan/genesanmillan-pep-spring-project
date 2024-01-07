package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers an Account and persists it
     * @param account Account to register
     * @return Persisted Account
     */
    public Account register(Account account) {
        return repository.save(account);
    }

    /**
     * Checks the database if the given Account username is available to use 
     * @param username String to check
     * @return true or false whether the account is available
     */
    public boolean usernameAvailable(String username) {
        for(Account a : repository.findAll()) {
            if(a.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the database if the given Account ID is available to use 
     * @param account_id Integer to check
     * @return true or false whether the account is available
     */
    public boolean usernameAvailable(int account_id) {
        return repository.findById(account_id).isEmpty();
    }

    /**
     * Checks the basic constraints for the Account given if it passes or not
     * @param account Account to be checked
     * @return true or false whether the account passes the constraints or not
     */
    public boolean constraintsPassed(Account account) {
        if(account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return false;
        }
        return true;
    }

    /**
     * Gets the Account data for the given username if it exists
     * @param username String to search for
     * @return Account object of the username, null if it doesn't exist
     */
    public Account getAccountByUsername(String username) {
        for(Account a : repository.findAll()) {
            if(a.getUsername().equals(username)) {
                return a;
            }
        }
        return null;
    }
}
