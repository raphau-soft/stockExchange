package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.TradeServiceImpl;
import com.raphau.springboot.stockExchange.service.TradingThread;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TradingThread tradingThread;

    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        return ResponseEntity.ok(testService.getTest());
    }

    @PostMapping("/setName")
    public void setName(@RequestBody String name){
        tradingThread.name = name;
    }

}
