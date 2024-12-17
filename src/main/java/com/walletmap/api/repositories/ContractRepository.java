package com.walletmap.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walletmap.api.models.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Transactional
    void deleteBySideBSharedId(Long userId);

}