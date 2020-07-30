package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SellOfferRestController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @PostMapping("/sellOffer")
    public TestDetailsDTO addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) {
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        sellOfferDTO.setId(0);
        long timeBase = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findById(sellOfferDTO.getStock_id());
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stockOptional.get());
        sellOfferRepository.save(sellOffer);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

}



























