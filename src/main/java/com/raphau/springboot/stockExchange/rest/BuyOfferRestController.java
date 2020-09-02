package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addOffer(@RequestBody BuyOfferDTO buyOfferDTO){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long timeBase = System.currentTimeMillis();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Company> company = companyRepository.findById(buyOfferDTO.getCompany_id());

        BuyOffer buyOffer = new BuyOffer(0, company.get(), user.get(),
                buyOfferDTO.getMaxPrice(), buyOfferDTO.getAmount(), buyOfferDTO.getDateLimit());
        buyOfferRepository.save(buyOffer);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return ResponseEntity.ok(testDetailsDTO);
    }

}
