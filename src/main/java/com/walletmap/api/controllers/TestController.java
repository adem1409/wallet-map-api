package com.walletmap.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.services.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TestController {

    @Autowired
    private ContractService contractService;

    @RequestMapping("/test")
    public String test() {
        contractService.deleteBySideBSharedId(Long.valueOf(2));
        contractService.deleteBySideBSharedId(Long.valueOf(3));
        contractService.deleteBySideBSharedId(Long.valueOf(4));
        return "Test";
    }

    @GetMapping("/test/get")
    public String getMethodName(@RequestParam String param) {
        // return json
        return "{\"param\":\"" + param + "\"}";
    }

}
