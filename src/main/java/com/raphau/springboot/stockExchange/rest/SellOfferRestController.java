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
import com.raphau.springboot.stockExchange.exception.CompanyNotFoundException;
import com.raphau.springboot.stockExchange.exception.StockAmountException;
import com.raphau.springboot.stockExchange.exception.StockNotFoundException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.TradeServiceImpl;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SellOfferRestController {

    @Autowired
    private TradeServiceImpl tradeService;

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
    public ResponseEntity<?> addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) throws InterruptedException {
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
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        if(!companyOptional.isPresent()){
            throw new CompanyNotFoundException("Company with id " + sellOfferDTO.getCompany_id() + " not found");
        }
        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(companyOptional.get(), userOpt.get());
        if(!stockOptional.isPresent()){
            throw new StockNotFoundException("User " + userOpt.get().getUsername() + " doesn't have stocks of " + companyOptional.get().getName());
        }
        Stock stock = stockOptional.get();
        if(sellOfferDTO.getAmount() > stock.getAmount() || sellOfferDTO.getAmount() <= 0){
            throw new StockAmountException("Wrong amount of resources");
        }
        stock.setAmount(stock.getAmount() - sellOfferDTO.getAmount());
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stock);
        timeBase = System.currentTimeMillis();
        stockRepository.save(stock);
        sellOfferRepository.save(sellOffer);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase + testDetailsDTO.getDatabaseTime());
        tradeService.trade(sellOfferDTO.getCompany_id());

        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

    private Optional<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userService.findByUsername(userDetails.getUsername());
    }

}



























