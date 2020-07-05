package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public User find(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());

        if (!user.isPresent()){
            throw new RuntimeException("User nof found");
        }

        return user.get();
    }

    @GetMapping("/user/buyOffers")
    public List<BuyOffer> findBuyOffers(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> user = userService.findByUsername(userDetails.getUsername());

        return user.get().getBuyOffers();
    }

    @GetMapping("/user/resources")
    public List<Stock> findResources(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        return user.getStocks();
    }

    @GetMapping("/user/sellOffers")
    public List<SellOffer> findSellOffers(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return null;

        List<SellOffer> sellOffers = new ArrayList<>();
        List<Stock> stocks = user.getStocks();

        stocks.forEach(stock -> {sellOffers.addAll(stock.getSellOffers());});

        return sellOffers;
    }

    @DeleteMapping("user/sellOffers/{theId}")
    public void deleteSellOffer(@PathVariable int theId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return;

        Optional<SellOffer> sellOfferOptional = sellOfferRepository.findById(theId);

        if(sellOfferOptional.isPresent()) {
            sellOfferRepository.deleteById(theId);
        }
    }

    @DeleteMapping("user/buyOffers/{theId}")
    public void deleteBuyOffer(@PathVariable int theId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return;

        Optional<BuyOffer> buyOfferOptional = buyOfferRepository.findById(theId);

        if(buyOfferOptional.isPresent()) {
            buyOfferRepository.deleteById(theId);
        }
    }

    @PutMapping("user/login")
    public void updateUser(@RequestBody UserUpdDTO userUpdDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        User user;

        if(userOpt.isPresent()){
            user = userOpt.get();
        } else return;

        user.setUsername(userUpdDTO.getLogin());
        user.setPassword(userUpdDTO.getPassword());

        userRepository.save(user);
    }
    // TODO: return TestDetails in every EndPoint
}
