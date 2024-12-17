package com.walletmap.api.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.models.Contact;
import com.walletmap.api.models.Contract;
import com.walletmap.api.models.Transaction;
import com.walletmap.api.models.Contract;
import com.walletmap.api.models.User;
import com.walletmap.api.services.ContactService;
import com.walletmap.api.services.ContractService;
import com.walletmap.api.services.TransactionService;
import com.walletmap.api.services.UserService;
import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.lib.Helpers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body, HttpServletResponse response,
            HttpServletRequest request) {
        try {

            User user = authHelpers.getAuthenticatedUser(request);

            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            boolean isShared = body.get("isShared").equals("true");

            Contract newContract = new Contract();
            newContract.setName(body.get("contractName"));
            newContract.setCurrency(body.get("currency"));
            newContract.setShared(isShared);
            newContract.setNetBalance(0.0);
            newContract.setCreationDate(LocalDate.now());
            newContract.setSideA(user);

            if (isShared) {
                Optional<User> userOptional = userService.findById(Long.parseLong(body.get("user")));
                newContract.setSideBShared(userOptional.get());
            } else {
                Optional<Contact> contactOptional = contactService.findById(Long.parseLong(body.get("contact")));
                newContract.setSideBLocal(contactOptional.get());

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
