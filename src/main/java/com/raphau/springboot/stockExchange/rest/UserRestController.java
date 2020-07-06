package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @GetMapping("/user")
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
        Map<String, Object> objects = new HashMap<>();
        objects.put("user", user.get());
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return objects;
    }

    @GetMapping("/user/buyOffers")
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

        Map<String, Object> objects = new HashMap<>();
        objects.put("buyOffers", user.getBuyOffers());
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return objects;
    }

    @GetMapping("/user/resources")
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

        Map<String, Object> objects = new HashMap<>();
        objects.put("stock", user.getStocks());
        objects.put("testDetails", testDetailsDTO);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return objects;
    }

    @GetMapping("/user/sellOffers")
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
            sellOfferRepository.deleteById(theId);
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @DeleteMapping("user/buyOffers/{theId}")
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
            buyOfferRepository.deleteById(theId);
        }
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

    @PutMapping("user/login")
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

        user.setUsername(userUpdDTO.getLogin());
        user.setPassword(userUpdDTO.getPassword());

        userRepository.save(user);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }
}
