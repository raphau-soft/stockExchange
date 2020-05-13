package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
}
