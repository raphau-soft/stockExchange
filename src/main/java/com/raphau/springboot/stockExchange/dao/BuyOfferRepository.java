package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.BuyOffer;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyOfferRepository extends JpaRepository<BuyOffer, Integer> {

    List<BuyOffer> findByUser(User user);

}
