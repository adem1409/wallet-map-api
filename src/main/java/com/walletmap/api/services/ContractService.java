package com.walletmap.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walletmap.api.models.Contract;
import com.walletmap.api.repositories.ContractRepository;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;

    public List<Contract> getAll() {
        return contractRepository.findAll();
    }

    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    // Find contact by ID
    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    // Delete contact
    public void deleteById(Long id) {
        contractRepository.deleteById(id);
    }

    public void deleteBySideBSharedId(Long userId) {
        contractRepository.deleteBySideBSharedId(userId);
    }

    public List<Contract> getBySideAId(Long userId) {
        return contractRepository.getBySideAId(userId);
    }

}