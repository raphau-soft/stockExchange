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
    private TradingThread tradingThread;

    public void trade(int companyId) throws InterruptedException {
        createSemaphore(companyId);
        tradingThread.run(companyId, companySemaphores.get(companyId));
    }

    private void createSemaphore(int companyId){
        if (companySemaphores.containsKey(companyId)) {
            return;
        }
        Semaphore semaphore = new Semaphore(1);
        companySemaphores.put(companyId, semaphore);
    }

}
