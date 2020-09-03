package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.*;
import com.raphau.springboot.stockExchange.exception.StockRateNotFoundException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRateRepository stockRateRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @GetMapping("/user")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> find(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if (!user.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        User userO = user.get();
        userO.setPassword(null);

        Map<String, Object> objects = new HashMap<>();
        objects.put("user", userO);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/user/buyOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findBuyOffers(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;
        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        user = userOpt.get();


        user.setPassword(null);
        Map<String, Object> objects = new HashMap<>();
        objects.put("buyOffers", user.getBuyOffers());
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return ResponseEntity.ok(objects);
    }

    @GetMapping("/user/resources")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findResources(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        user = userOpt.get();

        List<Stock> stocks = user.getStocks();
        List<StockRate> stockRates = new ArrayList<>();

        for (Stock stock : stocks) {
            Optional<StockRate> stockRate = stockRateRepository.findByCompanyAndActual(stock.getCompany(), true);
            if(!stockRate.isPresent()){
                throw new StockRateNotFoundException("Actual stock rate for " + stock.getCompany().getName() + " not found");
            }
            stockRates.add(stockRate.get());
        }

        Map<String, Object> objects = new HashMap<>();
        objects.put("stock", stocks);
        objects.put("stockRate", stockRates);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return ResponseEntity.ok(objects);
    }

    @GetMapping("/user/sellOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> findSellOffers(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        user = userOpt.get();

        List<SellOffer> sellOffers = new ArrayList<>();
        List<Stock> stocks = user.getStocks();

        stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));

        Map<String, Object> objects = new HashMap<>();
        objects.put("sellOffers", sellOffers);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return ResponseEntity.ok(objects);
    }

    @DeleteMapping("user/sellOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteSellOffer(@PathVariable int theId){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        user = userOpt.get();
        Optional<SellOffer> sellOfferOptional = sellOfferRepository.findById(theId);

        if(sellOfferOptional.isPresent()) {
            SellOffer sellOffer = sellOfferOptional.get();
            Stock stock = sellOffer.getStock();
            if(stock.getUser().getId() == user.getId()) {
                stock.setAmount(stock.getAmount() + sellOffer.getAmount());
                stockRepository.save(stock);
                sellOfferRepository.deleteById(theId);
            }
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

    @DeleteMapping("user/buyOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteBuyOffer(@PathVariable int theId){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }
        user = userOpt.get();
        Optional<BuyOffer> buyOfferOptional = buyOfferRepository.findById(theId);

        if(buyOfferOptional.isPresent()) {
            if(user.getId() == buyOfferOptional.get().getUser().getId())
                user.setMoney(user.getMoney().add(buyOfferOptional.get().getMaxPrice().multiply(BigDecimal.valueOf(buyOfferOptional.get().getAmount()))));
                buyOfferRepository.deleteById(theId);
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

    @PutMapping("user/login")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdDTO userUpdDTO){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(!userOpt.isPresent()){
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        user = userOpt.get();

        user.setUsername(userUpdDTO.getUsername());
        user.setPassword(userUpdDTO.getPassword());

        userRepository.save(user);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return ResponseEntity.ok(testDetailsDTO);
    }

}




















