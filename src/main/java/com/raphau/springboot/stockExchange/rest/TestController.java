package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        return ResponseEntity.ok(testService.getTest());
    }

}
