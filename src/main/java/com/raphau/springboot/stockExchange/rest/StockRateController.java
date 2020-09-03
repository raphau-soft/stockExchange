package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.entity.StockRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockRateController {
    @Autowired
    private StockRateRepository stockRateRepository;

    @GetMapping("/stockRates")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findStockRates(){
        List<StockRate> stockRateList = stockRateRepository.findByActual(true);
        return ResponseEntity.ok(stockRateList);
    }
}
