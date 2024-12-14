package com.walletmap.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.models.Contact;
import com.walletmap.api.services.ContactService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Contact contact, HttpServletResponse response) {
        try {

            Contact newContact = new Contact();
            newContact.setName(contact.getName());
            Contact savedContact = contactService.saveContact(newContact);

            return new ResponseEntity<>(savedContact, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
