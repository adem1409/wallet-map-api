package com.walletmap.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.walletmap.api.models.Contact;
import com.walletmap.api.models.User;
import com.walletmap.api.services.ContactService;
import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.lib.FileManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private AuthHelpers authHelpers;

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @Data
    public static class CreateRequest {
        private String name;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateRequest contact, HttpServletResponse response,
            HttpServletRequest request) {
        try {

            User user = authHelpers.getAuthenticatedUser(request);

            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            Contact newContact = new Contact();

            newContact.setName(contact.getName());
            newContact.setUser(user);
            newContact.setPicture("/uploads/contact-pictures/avatar_placeholder.png");

            Contact savedContact = contactService.save(newContact);

            return new ResponseEntity<>(savedContact, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-picture/{id}")
    public ResponseEntity<?> updatePicture(
            @RequestParam("picture") MultipartFile file,
            @PathVariable Long id,
            HttpServletRequest request) {
        try {

            User currentUser = authHelpers.getAuthenticatedUser(request);

            if (currentUser == null) {
                return ResponseEntity.status(401).body("Not authenticated");
            }

            Optional<Contact> optionalContact = contactService.findById(id);

            if (optionalContact.isEmpty()) {
                return ResponseEntity.status(404).body("Contact Not Found");
            }

            Contact currentContact = optionalContact.get();

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            String uploadDir = "/uploads/contact-pictures/";

            // Use FilesManager to upload the file
            String uploadedFilePath = FileManager.uploadFile(file, uploadDir);
            System.out.println("--------------- uploadedFilePath");
            System.out.println(uploadedFilePath);

            // Update the contact's picture field
            currentContact.setPicture(uploadedFilePath);
            contactService.save(currentContact);

            return ResponseEntity.ok(Map.of("message", "Photo uploaded successfully", "filePath", uploadedFilePath));
        } catch (

        Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id, HttpServletRequest request) {

        try {
            // Fetch the contact by ID
            Optional<Contact> contactOpt = contactService.findById(id);

            if (contactOpt.isEmpty()) {
                return new ResponseEntity<>(Map.of("message", "Contact not found"), HttpStatus.NOT_FOUND);
            }

            Contact contact = contactOpt.get();

            // Get the authenticated user
            User user = authHelpers.getAuthenticatedUser(request);

            // Check if the authenticated user is the creator of the contact
            if (!user.getId().equals(contact.getUser().getId())) {
                return new ResponseEntity<>(Map.of("message", "Unauthorized to delete this contact"),
                        HttpStatus.FORBIDDEN);
            }

            // Delete the contact
            contactService.deleteContactById(id);
            return new ResponseEntity<>(Map.of("message", "Contact deleted successfully"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

}
