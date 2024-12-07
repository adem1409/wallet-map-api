package com.walletmap.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/test/get")
    public String getMethodName(@RequestParam String param) {
        // return json
        return "{\"param\":\"" + param + "\"}";
    }

}
