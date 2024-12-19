package com.walletmap.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walletmap.api.models.Transaction;
import com.walletmap.api.repositories.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByContractId(Long contractId) {
        return transactionRepository.findByContractId(contractId);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
