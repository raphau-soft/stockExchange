package com.raphau.springboot.stockExchange.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class SellOfferDTO {

    private int id;
    private int stock_id;
    private BigDecimal minPrice;
    private int amount;
    private Date dateLimit;

    public SellOfferDTO() {
    }

    public SellOfferDTO(int id, int stock_id, BigDecimal minPrice, int amount, Date dateLimit) {
        this.id = id;
        this.stock_id = stock_id;
        this.minPrice = minPrice;
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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
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
                ", maxPrice=" + minPrice +
                ", amount=" + amount +
                ", dateLimit=" + dateLimit +
                '}';
    }
}
