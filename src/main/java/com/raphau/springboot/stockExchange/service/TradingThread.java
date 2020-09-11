package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.dao.*;
import com.raphau.springboot.stockExchange.entity.*;

import javax.jws.soap.SOAPBinding;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Semaphore;

public class TradingThread implements Runnable {

    private final int OFFERS_NUMBER = 5;

    private int companyId;
    private Semaphore semaphore;
    private BuyOfferRepository buyOfferRepository;
    private StockRepository stockRepository;
    private SellOfferRepository sellOfferRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private List<BuyOffer>  buyOffers = new ArrayList<>();
    private List<SellOffer>  sellOffers = new ArrayList<>();
    private List<Stock>  stocks = new ArrayList<>();

    public TradingThread(int companyId, Semaphore semaphore, BuyOfferRepository buyOfferRepository,
                         StockRepository stockRepository, SellOfferRepository sellOfferRepository,
                         UserRepository userRepository, TransactionRepository transactionRepository){
        this.companyId = companyId;
        this.semaphore = semaphore;
        this.buyOfferRepository = buyOfferRepository;
        this.stockRepository = stockRepository;
        this.sellOfferRepository = sellOfferRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run() {
        try {
            System.out.println("\n\n\n Trying to acquire semaphore for company " + companyId + "\n\n\n");
            semaphore.acquire();
            System.out.println("\n\n\n Acquired semaphore for company " + companyId + "\n\n\n");

            // Trading logic is here
            // check if there is 5 buy offers and 5 sell offers
            getBuyOffers();
            System.out.println("\n\n\n Amount of buyoffers - " + buyOffers.size() + "\n\n\n");
            getSellOffers();

            // sort'em
            buyOffers.sort(new SortBuyOffers());
            sellOffers.sort(new SortSellOffers());
            // trade with two variants (more stocks on sell/buy offer)
//            while(buyOffers.size() >= OFFERS_NUMBER && sellOffers.size() >= OFFERS_NUMBER &&
//                    startTrading(buyOffers.subList(0, OFFERS_NUMBER), sellOffers.subList(0, OFFERS_NUMBER))){
//                System.out.println(buyOffers.size() + " - " + sellOffers.size());
//            };
            // update stock rate

            // はじめまして

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private boolean startTrading(List<BuyOffer> buyOffers, List<SellOffer> sellOffers) {
        for (SellOffer sellOffer : sellOffers) {
            for (BuyOffer buyOffer : buyOffers) {
                if (sellOffer.getMinPrice().compareTo(buyOffer.getMaxPrice()) < 0) {
                    if (sellOffer.getAmount() < buyOffer.getAmount()) {
                        buyOfferStay(buyOffer, sellOffer);
                        sellOffers.remove(sellOffer);
                    } else if(sellOffer.getAmount() > buyOffer.getAmount()){
                        sellOfferStay(buyOffer, sellOffer);
                        buyOffers.remove(buyOffer);
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private void buyOfferStay(BuyOffer buyOffer, SellOffer sellOffer){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, sellOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(sellOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * sellOffer.getAmount())));
        buyOffer.setAmount(buyOffer.getAmount() - sellOffer.getAmount());
        sellOffer.setAmount(0);
        sellOffer.setActual(false);
        transactionRepository.save(transaction);
        buyOfferRepository.save(buyOffer);
        sellOfferRepository.save(sellOffer);
    }
    private void sellOfferStay(BuyOffer buyOffer, SellOffer sellOffer){
        double price = (buyOffer.getMaxPrice().doubleValue() + sellOffer.getMinPrice().doubleValue())/2;
        Transaction transaction = new Transaction(0, buyOffer, sellOffer, buyOffer.getAmount(), price, new Date());
        User sellOfferOwner = sellOffer.getStock().getUser();
        User buyOfferOwner = buyOffer.getUser();
        buyOfferOwner.setMoney(buyOfferOwner.getMoney().add(buyOffer.getMaxPrice()
                .subtract(new BigDecimal(price)).multiply(new BigDecimal(buyOffer.getAmount()))));
        sellOfferOwner.setMoney(sellOfferOwner.getMoney().add(new BigDecimal(price * buyOffer.getAmount())));
        sellOffer.setAmount(sellOffer.getAmount() - buyOffer.getAmount());
        buyOffer.setAmount(0);
        buyOffer.setActual(false);
        transactionRepository.save(transaction);
        sellOfferRepository.save(sellOffer);
        buyOfferRepository.save(buyOffer);
    }

    private void getBuyOffers(){
        buyOffers = buyOfferRepository.findByCompany_IdAndActual(companyId, true);
    }

    private void getSellOffers(){
        stocks = stockRepository.findByCompany_Id(companyId);
        stocks.forEach(stock -> sellOffers.addAll(stock.getSellOffers()));
        sellOffers.removeIf(sellOffer -> !sellOffer.isActual());
    }

    static class SortBuyOffers implements Comparator<BuyOffer>{
        public int compare(BuyOffer a, BuyOffer b){
            return b.getMaxPrice().compareTo(a.getMaxPrice());
        }
    }

    class SortSellOffers implements Comparator<SellOffer>{
        public int compare(SellOffer a, SellOffer b){
            return a.getMinPrice().compareTo(b.getMinPrice());
        }
    }

}
