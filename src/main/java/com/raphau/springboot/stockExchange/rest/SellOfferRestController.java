package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SellOfferRestController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/sellOffer")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) {
        long timeApp = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTime(sellOfferDTO.getDateLimit());
        c.add(Calendar.DATE, 1);
        sellOfferDTO.setDateLimit(c.getTime());
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        sellOfferDTO.setId(0);
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = getUser();
        Optional<Company> companyOptional = companyRepository.findById(sellOfferDTO.getCompany_id());
        if(!companyOptional.isPresent() || !userOpt.isPresent()){
            return ResponseEntity.badRequest().body("Company or user doesn't exist");
        }
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(companyOptional.get(), userOpt.get());
        if(!stockOptional.isPresent()){
            return ResponseEntity.badRequest().body("You are not the owner of this resource");
        }
        if(sellOfferDTO.getAmount() > stockOptional.get().getAmount()){
            return ResponseEntity.badRequest().body("Wrong resources amount");
        }
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stockOptional.get());
        sellOfferRepository.save(sellOffer);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

    private Optional<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        return userOpt;
    }

}



























