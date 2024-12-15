package com.walletmap.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walletmap.api.models.Contact;
import com.walletmap.api.repositories.ContactRepository;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Find contact by ID
    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    // Delete contact
    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }

}