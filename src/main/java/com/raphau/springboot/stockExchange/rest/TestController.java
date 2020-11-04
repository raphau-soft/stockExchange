package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.service.TradingThread;
import com.raphau.springboot.stockExchange.service.ints.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TradingThread tradingThread;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/getTest")
    public ResponseEntity<?> getTest(){
        return ResponseEntity.ok(testService.getTest());
    }

    @PostMapping("/setName")
    public void setName(@RequestBody String name){
        tradingThread.name = name;
    }

    @PostMapping("/cleanDB")
    public void cleanDB(){
        testService.cleanTestDB();
    }

    @PostMapping("/restartDB")
    public void restartDB(){
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("restart.sql"));
        resourceDatabasePopulator.execute(dataSource);
        resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("reinsert.sql"));
        resourceDatabasePopulator.execute(dataSource);
    }

}
