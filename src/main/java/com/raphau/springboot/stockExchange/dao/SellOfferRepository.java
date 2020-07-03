package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.SellOffer;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellOfferRepository extends JpaRepository<SellOffer, Integer> {

//    Optional<List<SellOffer>> findAllBy

}
