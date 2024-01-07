package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates new message and persists it
     * @param message Message object to add
     * @return Persisted Message
     */
    public Message createMessage(Message message) {
        return repository.save(message);
    }

    /**
     * Checks the basic constraints for the String given if it passes or not
     * @param message_text String to be checked
     * @return true or false whether the message text passes the constraints or not
     */
    public boolean constraintsPassed(String message_text) {
        if(message_text.isBlank() || message_text.length() >= 255) {
            return false;
        }
        return true;
    }

    /**
     * Get all the messages from the database
     * @return list of all the messages
     */
    public List<Message> getMessages() {
        return repository.findAll();
    }

    /**
     * Get a Message object with the specific ID
     * @param message_id ID to use for retrieval
     * @return Message with the given message_id, or null if not found
     */
    public Message getMessage(int message_id) {
        return repository.findById(message_id).orElse(null);
    }

    /**
     * Delete a message with the given message_id
     * @param message_id Integer to be utilized
     * @return true if Message existed and deleted, false if it didn't exist
     */
    public boolean deleteMessage(int message_id) {
        if(getMessage(message_id) != null) {
            repository.deleteById(message_id);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Updates message_text of the Message object with the given message_id
     * @param message_id Integer to find the Message
     * @param message_text String to replace the old message_text
     * @return true if it successfully updated, false if not
     */
    public boolean updateMessage(int message_id, String message_text) {
        Message message = getMessage(message_id);
        message_text = message_text != null ? message_text : "";
        if(message != null && !message_text.isBlank() && message_text.length() <= 255) {
            message.setMessage_text(message_text);
            repository.save(message);
            return true;
        }
        return false;
    }

    /**
     * Finds all the messages from the given posted_by user
     * @param posted_by Integer identifier for the messages
     * @return List of all the Message objects from user
     */
    public List<Message> getAllMessages(int posted_by) {
        List<Message> messages = new ArrayList<>();
        
        for(Message m : repository.findAll()) {
            if(m.getPosted_by() == posted_by) {
                messages.add(m);
            }
        }

        return messages;
    }
}
