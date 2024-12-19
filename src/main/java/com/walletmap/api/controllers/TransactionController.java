package com.walletmap.api.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.controllers.ContractController.CreateContractRequest;
import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.models.Contact;
import com.walletmap.api.models.Contract;
import com.walletmap.api.models.Transaction;
import com.walletmap.api.models.User;
import com.walletmap.api.repositories.TransactionRepository;
import com.walletmap.api.services.ContactService;
import com.walletmap.api.services.ContractService;
import com.walletmap.api.services.TransactionService;
import com.walletmap.api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthHelpers authHelpers;

    @Data
    public static class CreateTransactionRequest {
        private Double amount;
        private Long contract;
        private LocalDateTime date;
        private String label;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateTransactionRequest body, HttpServletResponse response,
            HttpServletRequest request) {
        System.out.println(body);
        try {

            User user = authHelpers.getAuthenticatedUser(request);
            if (user == null) {
                return new ResponseEntity<>(Map.of("message", "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            Optional<Contract> contractOpt = contractService.findById(body.getContract());
            if (contractOpt.isEmpty()) {
                return new ResponseEntity<>(Map.of("message", "Contract not found"), HttpStatus.NOT_FOUND);
            }
            Contract contract = contractOpt.get();

            Transaction newTransaction = new Transaction();
            newTransaction.setAmount(body.getAmount());
            newTransaction.setLabel(body.getLabel());
            newTransaction.setDate(body.getDate());
            newTransaction.setCreator(user);
            newTransaction.setContract(contract);

            if (contract.isShared()) {
                newTransaction.setStatus(Transaction.Status.PENDING);
            } else {
                newTransaction.setStatus(Transaction.Status.ACCEPTED);
            }

            Transaction transaction = transactionService.save(newTransaction);

            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
