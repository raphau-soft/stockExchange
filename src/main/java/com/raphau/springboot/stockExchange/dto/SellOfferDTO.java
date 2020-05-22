package com.raphau.springboot.stockExchange.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class SellOfferDTO {

    private int id;
    private int stock_id;
    private BigDecimal maxPrice;
    private int amount;
    private Date dateLimit;

    public SellOfferDTO() {
    }

    public SellOfferDTO(int id, int stock_id, BigDecimal maxPrice, int amount, Date dateLimit) {
        this.id = id;
        this.stock_id = stock_id;
        this.maxPrice = maxPrice;
        this.amount = amount;
        this.dateLimit = dateLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
    }

    @Override
    public String toString() {
        return "SellOfferDTO{" +
                "id=" + id +
                ", stock_id=" + stock_id +
                ", maxPrice=" + maxPrice +
                ", amount=" + amount +
                ", dateLimit=" + dateLimit +
                '}';
    }
}
