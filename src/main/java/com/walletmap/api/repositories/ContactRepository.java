package com.walletmap.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walletmap.api.models.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}