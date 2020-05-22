package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dao.SellOfferRepository;
import com.raphau.springboot.stockExchange.dao.StockRepository;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;
import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SellOfferRestController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    @PostMapping("/sellOffer")
    public void addSellOffer(@RequestBody SellOfferDTO sellOfferDTO) {
        sellOfferDTO.setId(0);
        Optional<Stock> stockOptional = stockRepository.findById(sellOfferDTO.getStock_id());
        SellOffer sellOffer = new SellOffer(sellOfferDTO, stockOptional.get());
        sellOfferRepository.save(sellOffer);
    }

}



























