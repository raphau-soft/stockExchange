package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Semaphore;

@Service
public class TradingThread {

    private final int OFFERS_NUMBER = 5;

    @Autowired
    private BuyOfferRepository buyOfferRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private SellOfferRepository sellOfferRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockRateRepository stockRateRepository;
    @Autowired
    private TestRepository testRepository;


    @Async("asyncExecutor")
    public void run(int companyId, Semaphore semaphore) {
        long timeApp = System.currentTimeMillis();
        Test test = new Test();
        try {
            System.out.println("\n\n\n Trying to acquire semaphore for company " + companyId + "\n\n\n");
            semaphore.acquire();
            System.out.println("\n\n\n Acquired semaphore for company " + companyId + "\n\n\n");

            List<SellOffer>  sellOffers = new ArrayList<>();

            // Trading logic is here
            // check if there are 5 buy offers and 5 sell offers
            long timeDB = System.currentTimeMillis();
            List<BuyOffer> buyOffers = new ArrayList<>(buyOfferRepository.findByCompany_IdAndActual(companyId, true));
            List<Stock> stocks = stockRepository.findByCompany_Id(companyId);
            test.setDatabaseTime(System.currentTimeMillis() - timeDB);

            System.out.println("\n\n\n Amount of buyoffers - " + buyOffers.size() + "\n\n\n");

            stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
            sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
            System.out.println("\n\n\n Amount of selloffers - " + sellOffers.size() + "\n\n\n");

            // sort'em
            buyOffers.sort(new SortBuyOffers());
            sellOffers.sort(new SortSellOffers());
            List<Transaction> transactions = new ArrayList<>();
            // trade with two variants (more stocks on sell/buy offer)
            while(buyOffers.size() >= OFFERS_NUMBER && sellOffers.size() >= OFFERS_NUMBER){
                if(!startTrading(buyOffers.subList(0, OFFERS_NUMBER), sellOffers.subList(0, OFFERS_NUMBER), transactions, test)) break;
                sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
                buyOffers.removeIf(buyOffer -> !buyOffer.isActual());
            }

            // update stock rate
            if(!transactions.isEmpty())
                updateStockRates(companyId, transactions, test);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        test.setApplicationTime(System.currentTimeMillis() - timeApp);
        testRepository.save(test);
    }

    private void updateStockRates(int companyId, List<Transaction> transactions, Test test){
        double price = 0;
        int amount = 0;
        for(Transaction transaction: transactions){
            price += transaction.getPrice() * transaction.getAmount();
            amount += transaction.getAmount();
        }

        double average = price / amount;
        long timeDB = System.currentTimeMillis();
        Optional<StockRate> stockRateOptional = stockRateRepository.findByCompany_IdAndActual(companyId, true);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);

        if(stockRateOptional.isPresent()){
            StockRate stockRate = stockRateOptional.get();
            double dif = average - stockRate.getRate();
            dif = dif/10;
            double newPrice = stockRate.getRate() + dif;
            newPrice = Math.round(newPrice*100) / 100.0;
            StockRate newStockRate = new StockRate(0, stockRate.getCompany(), newPrice, new Date(), true);
            stockRate.setActual(false);
            timeDB = System.currentTimeMillis();
            stockRateRepository.save(stockRate);
            stockRateRepository.save(newStockRate);
            test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        }

    }

    private boolean startTrading(List<BuyOffer> buyOffers, List<SellOffer> sellOffers, List<Transaction> transactions, Test test) {
        BuyOffer buyOffer;
        SellOffer sellOffer;
        int i=0,j=0;
        while(i < buyOffers.size() && j < sellOffers.size()){
            buyOffer = buyOffers.get(i);
            sellOffer = sellOffers.get(j);
            if(buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) < 0) return false;
            if(buyOffer.getAmount() > sellOffer.getAmount()){
                transactions.add(buyOfferStay(buyOffer, sellOffer, test));
                j++;
            } else if (buyOffer.getAmount() < sellOffer.getAmount()){
                transactions.add(sellOfferStay(buyOffer, sellOffer, test));
                i++;
            } else {
                transactions.add(noneOfferStay(buyOffer,sellOffer, test));
                i++;j++;
            }
        }


//        for(int i = 0; i < buyOffers.size();){
//            buyOffer = buyOffers.get(i);
//            for(int j = 0; j < sellOffers.size();){
//                sellOffer = sellOffers.get(j);
//                if(!sellOffer.isActual()) continue;
//                if(buyOffer.getMaxPrice().compareTo(sellOffer.getMinPrice()) < 0) return false;
//                if(buyOffer.getAmount() > sellOffer.getAmount()){
//                    buyOfferStay(buyOffer, sellOffer);
//                    j++;
//                } else if (buyOffer.getAmount() < sellOffer.getAmount()){
//                    sellOfferStay(buyOffer, sellOffer);
//                    i++;
//                } else {
//                    noneOfferStay(buyOffer,sellOffer);
//                    i++;j++;
//                }
//            }
//        }
        return true;
    }

    private Transaction noneOfferStay(BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * sellOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        System.out.println("\n\n\n Saved stock " + stock.toString() + "\n\n\n");
        timeDB = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        return transaction;
    }

    private Transaction buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * sellOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), sellOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + sellOffer.getAmount());
        }
        buyOffer.setAmount(buyOffer.getAmount() - sellOffer.getAmount());
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        timeDB = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        return transaction;
    }
    private Transaction sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer, Test test){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, buyOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(buyOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * buyOffer.getAmount())));
        long timeDB = System.currentTimeMillis();
        Optional<Stock> stockOptional = stockRepository.findByCompanyAndUser(buyOffer.getCompany(), buyOfferOwner);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        Stock stock;
        if(!stockOptional.isPresent()) {
            stock = new Stock(0, buyOfferOwner, buyOffer.getCompany(), buyOffer.getAmount());
        } else {
            stock = stockOptional.get();
            stock.setAmount(stock.getAmount() + buyOffer.getAmount());
        }
        sellOffer.setAmount(sellOffer.getAmount() - buyOffer.getAmount());
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        timeDB = System.currentTimeMillis();
        stockRepository.save(stock);
        transactionRepository.save(transaction);
        sellOfferRepository.save(sellOffer);
        buyOfferRepository.save(buyOffer);
        test.setDatabaseTime(test.getDatabaseTime() + System.currentTimeMillis() - timeDB);
        return transaction;
    }

    static class SortBuyOffers implements Comparator<BuyOffer>{
        public int compare(BuyOffer a, BuyOffer b){
            return b.getMaxPrice().compareTo(a.getMaxPrice());
        }
    }

    static class SortSellOffers implements Comparator<SellOffer>{
        public int compare(SellOffer a, SellOffer b){
            return a.getMinPrice().compareTo(b.getMinPrice());
        }
    }

}
