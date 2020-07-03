package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BuyOfferRestController {

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/buyOffer")
    public void addOffer(@RequestBody BuyOfferDTO buyOfferDTO){
        Optional<User> user = userRepository.findById(buyOfferDTO.getUser_id());
        Optional<Company> company = companyRepository.findById(buyOfferDTO.getCompany_id());

        BuyOffer buyOffer = new BuyOffer(0, company.get(), user.get(),
                buyOfferDTO.getMinPrice(), buyOfferDTO.getAmount(), buyOfferDTO.getDateLimit());
        buyOfferRepository.save(buyOffer);

    }

}
