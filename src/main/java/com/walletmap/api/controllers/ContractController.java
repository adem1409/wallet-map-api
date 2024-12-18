package com.walletmap.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.models.Contact;
import com.walletmap.api.models.Contract;
import com.walletmap.api.models.User;
import com.walletmap.api.services.ContactService;
import com.walletmap.api.services.ContractService;
import com.walletmap.api.services.TransactionService;
import com.walletmap.api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthHelpers authHelpers;

    @GetMapping
    public ResponseEntity<?> getUserContracts(HttpServletRequest request) {
        try {
            User user = authHelpers.getAuthenticatedUser(request);

            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            List<Contract> contracts = contractService.getBySideAId(user.getId());
            return new ResponseEntity<>(contracts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    public static class CreateContractRequest {
        private String contractName;
        private String currency;
        public boolean isShared;
        private Long user;
        private Long contact;
        private String contactName;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateContractRequest body, HttpServletResponse response,
            HttpServletRequest request) {
        System.out.println(body);
        try {

            User user = authHelpers.getAuthenticatedUser(request);

            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            Contract newContract = new Contract();
            newContract.setName(body.getContractName());
            newContract.setCurrency(body.getCurrency());
            newContract.setShared(body.isShared);
            newContract.setNetBalance(0.0);
            newContract.setSideA(user);

            if (body.isShared) {
                Optional<User> userOptional = userService.findById(body.getUser());
                newContract.setSideBShared(userOptional.get());
            } else {
                if (body.getContactName().isEmpty()) {
                    Optional<Contact> contactOptional = contactService.findById(body.getContact());
                    newContract.setSideBLocal(contactOptional.get());
                } else {
                    Contact newContact = new Contact();
                    newContact.setName(body.getContactName());
                    newContact.setUser(user);
                    Contact savedContact = contactService.save(newContact);
                    newContract.setSideBLocal(savedContact);
                }

            }

            Contract savedContract = contractService.save(newContract);

            return new ResponseEntity<>(savedContract, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContract(@PathVariable Long id, HttpServletRequest request) {
        try {
            User user = authHelpers.getAuthenticatedUser(request);

            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            Optional<Contract> contract = contractService.findById(id);

            if (contract.isPresent()) {
                Contract c = contract.get();
                c.getSideBShared();
                return new ResponseEntity<>(contract.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Map.of("message", "Contract not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
