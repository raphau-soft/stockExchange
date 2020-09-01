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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public TestDetailsDTO addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) {
        System.out.println("\n\n\n\n");
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        sellOfferDTO.setId(0);
        long timeBase = System.currentTimeMillis();
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        Optional<Company> companyOptional = companyRepository.findById(sellOfferDTO.getCompany_id());
        if(!companyOptional.isPresent() || !userOpt.isPresent()){
            System.out.println("Nie ma firmy lub użytkownika");
            return null;
        }
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(companyOptional.get(), userOpt.get());
        if(!stockOptional.isPresent()){
            System.out.println("Nie ma akcji");
            return null;
        }
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stockOptional.get());
        sellOfferRepository.save(sellOffer);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        System.out.println("\n\n\n\n");
        return testDetailsDTO;
    }

}



























