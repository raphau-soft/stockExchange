package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
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

    // TODO: DeleteMapping for sell offers and buy offers
    // TODO: GetMapping user's stocks
    // TODO: PutMapping update login
    // TODO: return TestDetails in every EndPoint

//    @GetMapping("/user/{theId}")
//    public User findById(@PathVariable int theId){
//        User user = userService.findById(theId);
//        return user;
//    }
//
//    @PostMapping("/user")
//    public User addUser(@RequestBody User user){
//        user.setId(0);
//        userService.save(user);
//        return user;
//    }
//
//    @PutMapping("/user")
//    public User updateUser(@RequestBody User user){
//        userService.save(user);
//        return user;
//    }
//
//    @DeleteMapping("/user/{theId}")
//    public String deleteUser(@PathVariable int theId){
//        User user = userService.findById(theId);
//
//        if(user == null){
//            throw new RuntimeException("Didn't find user with given id: " + theId);
//        }
//
//        userService.deleteById(theId);
//
//        return "Deleted user with id: " + theId;
//    }
}
