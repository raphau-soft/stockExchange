package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

@Service
public class TradeServiceImpl {

    private final HashMap<Integer, Semaphore> companySemaphores = new HashMap<>();

    @Autowired
    private BuyOfferRepository buyOfferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockRateRepository stockRateRepository;

    @Autowired
    private SellOfferRepository sellOfferRepository;

    public void trade(int companyId) throws InterruptedException {
        createSemaphore(companyId);
        TradingThread thread = new TradingThread(companyId, companySemaphores.get(companyId), buyOfferRepository,
                stockRepository, sellOfferRepository, userRepository, transactionRepository, stockRateRepository);
        thread.run();
    }

    private void createSemaphore(int companyId){
        if (companySemaphores.containsKey(companyId)) {
            return;
        }
        Semaphore semaphore = new Semaphore(1);
        companySemaphores.put(companyId, semaphore);
    }

}
