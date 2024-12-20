package com.walletmap.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walletmap.api.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByContractId(Long contractId);

    List<Transaction> findByContractIdOrderByCreatedAtDesc(Long contractId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.contract.id = :contractId")
    Double findTotalAmountByContractId(@Param("contractId") Long contractId);

}
