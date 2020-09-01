package com.raphau.springboot.stockExchange.dao;

import com.raphau.springboot.stockExchange.entity.Company;
import com.raphau.springboot.stockExchange.entity.StockRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRateRepository extends JpaRepository<StockRate, Integer> {
    Optional<StockRate> findByCompanyAndActual(Company company, Boolean actual);
}
