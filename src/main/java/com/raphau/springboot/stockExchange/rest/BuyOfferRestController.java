package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuyOfferRestController {

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @PostMapping("/buyOffer")
    public void addOffer(@RequestBody BuyOffer buyOffer){
        buyOffer.setId(0);
        buyOfferRepository.save(buyOffer);
    }

}
