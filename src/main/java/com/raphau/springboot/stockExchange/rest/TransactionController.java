package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.TransactionRepository;
import com.raphau.springboot.stockExchange.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController
public class TransactionController implements Serializable {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findAllTransactions(){
        List<Transaction> transactions =  transactionRepository.findAll();
        return ResponseEntity.ok(transactions);
    }

}
