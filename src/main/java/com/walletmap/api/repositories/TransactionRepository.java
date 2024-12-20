package com.walletmap.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walletmap.api.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByContractId(Long contractId);

    List<Transaction> findByContractIdOrderByCreatedAtDesc(Long contractId);
}
