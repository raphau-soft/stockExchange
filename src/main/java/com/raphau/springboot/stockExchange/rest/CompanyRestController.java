package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.CompanyDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.StockRate;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@RestController
public class CompanyRestController implements Serializable {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockRateRepository stockRateRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/companies")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findAllCompanies(){
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @PostMapping("/company")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDTO){
        long timeApp = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        if (!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }
        User user = userOpt.get();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        companyDTO.setId(0);
        Company company = new Company(companyDTO.getId(), companyDTO.getName());
        Stock stock = new Stock(0, user, company, companyDTO.getAmount());
        StockRate stockRate = new StockRate(0, company, companyDTO.getPrice(), new Date(), true);
        long timeBase = System.currentTimeMillis();
        companyRepository.save(company);
        stockRepository.save(stock);
        stockRateRepository.save(stockRate);


        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

}
