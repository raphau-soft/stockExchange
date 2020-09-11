package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.BuyOfferRepository;
import com.raphau.springboot.stockExchange.dao.CompanyRepository;
import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.dto.BuyOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.exception.CompanyNotFoundException;
import com.raphau.springboot.stockExchange.exception.NotEnoughMoneyException;
import com.raphau.springboot.stockExchange.exception.UserNotFoundException;
import com.raphau.springboot.stockExchange.security.MyUserDetails;
import com.raphau.springboot.stockExchange.service.TradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BuyOfferRestController {

    @Autowired
    private TradeServiceImpl tradeService;

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/buyOffer")
    @CrossOrigin(value = "*", maxAge = 3600)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addOffer(@RequestBody BuyOfferDTO buyOfferDTO) throws InterruptedException {
        long timeApp = System.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.setTime(buyOfferDTO.getDateLimit());
        c.add(Calendar.DATE, 1);
        buyOfferDTO.setDateLimit(c.getTime());
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        long timeBase = System.currentTimeMillis();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        Optional<Company> companyOptional = companyRepository.findById(buyOfferDTO.getCompany_id());

        if(!companyOptional.isPresent()){
            throw new CompanyNotFoundException("Company with id " + buyOfferDTO.getCompany_id() + " not found");
        }
        if(!userOptional.isPresent()){
            throw new UserNotFoundException("User" + userDetails.getUsername() + " not found");
        }
        User user = userOptional.get();
        Company company = companyOptional.get();
        if(user.getMoney().compareTo(buyOfferDTO.getMaxPrice().multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))) < 0){
            throw new NotEnoughMoneyException("Not enough money");
        }

        BuyOffer buyOffer = new BuyOffer(0, company, user,
                buyOfferDTO.getMaxPrice(), buyOfferDTO.getAmount(), buyOfferDTO.getAmount(), buyOfferDTO.getDateLimit(), true);
        user.setMoney(user.getMoney().subtract(buyOfferDTO.getMaxPrice().multiply(BigDecimal.valueOf(buyOfferDTO.getAmount()))));
        userRepository.save(user);
        buyOfferRepository.save(buyOffer);
        tradeService.trade(buyOfferDTO.getCompany_id());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);

        return ResponseEntity.ok(testDetailsDTO);
    }

}
