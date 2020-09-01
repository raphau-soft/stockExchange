package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRateRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.*;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @GetMapping("/user")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> find(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        if (!user.isPresent()){
            throw new RuntimeException("User not found");
        }

        User userO = user.get();
        userO.setPassword(null);

        Map<String, Object> objects = new HashMap<>();
        objects.put("user", userO);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @GetMapping("/user/buyOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> findBuyOffers(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;
        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;
        user.setPassword(null);
        Map<String, Object> objects = new HashMap<>();
        objects.put("buyOffers", user.getBuyOffers());
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return objects;
    }

    @GetMapping("/user/resources")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> findResources(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        List<Stock> stocks = user.getStocks();
        List<StockRate> stockRates = new ArrayList<>();

        for (Stock stock : stocks) {
            stockRates.add(stockRateRepository.findByCompanyAndActual(stock.getCompany(), true).get());
        }

        Map<String, Object> objects = new HashMap<>();
        objects.put("stock", stocks);
        objects.put("stockRate", stockRates);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return objects;
    }

    @GetMapping("/user/sellOffers")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> findSellOffers(){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        List<SellOffer> sellOffers = new ArrayList<>();
        List<Stock> stocks = user.getStocks();

        stocks.forEach(stock -> {sellOffers.addAll(stock.getSellOffers());});

        Map<String, Object> objects = new HashMap<>();
        objects.put("sellOffers", sellOffers);
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return objects;
    }

    @DeleteMapping("user/sellOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public TestDetailsDTO deleteSellOffer(@PathVariable int theId){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        Optional<SellOffer> sellOfferOptional = sellOfferRepository.findById(theId);

        if(sellOfferOptional.isPresent()) {
            if(sellOfferOptional.get().getStock().getUser().getId() == user.getId())
                sellOfferRepository.deleteById(theId);
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @DeleteMapping("user/buyOffers/{theId}")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public TestDetailsDTO deleteBuyOffer(@PathVariable int theId){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        Optional<BuyOffer> buyOfferOptional = buyOfferRepository.findById(theId);

        if(buyOfferOptional.isPresent()) {
            if(user.getId() == buyOfferOptional.get().getUser().getId())
                buyOfferRepository.deleteById(theId);
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @PutMapping("user/login")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public TestDetailsDTO updateUser(@RequestBody UserUpdDTO userUpdDTO){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        user.setUsername(userUpdDTO.getUsername());
        user.setPassword(userUpdDTO.getPassword());

        userRepository.save(user);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }
}
