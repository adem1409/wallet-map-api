package com.walletmap.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.models.Contract;
import com.walletmap.api.repositories.ContractRepository;
import com.walletmap.api.repositories.TransactionRepository;
import com.walletmap.api.services.ContractService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TestController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/test")
    public String test() {
        contractService.deleteBySideBSharedId(Long.valueOf(2));
        contractService.deleteBySideBSharedId(Long.valueOf(3));
        contractService.deleteBySideBSharedId(Long.valueOf(4));
        return "Test";
    }

    @GetMapping("/update-net-balances")
    public ResponseEntity<String> updateNetBalances() {
        List<Contract> contracts = contractRepository.findAll();

        for (Contract contract : contracts) {
            Double totalAmount = transactionRepository.findTotalAmountByContractId(contract.getId());
            contract.setNetBalance(totalAmount != null ? totalAmount : 0.0);
        }

        contractRepository.saveAll(contracts);

        return ResponseEntity.ok("Net balances updated successfully!");
    }

    @GetMapping("/test/get")
    public String getMethodName(@RequestParam String param) {
        // return json
        return "{\"param\":\"" + param + "\"}";
    }

}
