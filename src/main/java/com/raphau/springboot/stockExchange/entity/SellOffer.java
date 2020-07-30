package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.raphau.springboot.stockExchange.dto.SellOfferDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="sell_offer")
public class SellOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Stock.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="stock_id", nullable = false)
    private Stock stock;

    @OneToMany(mappedBy = "sellOffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Transaction> transactions;

    @Column(name="amount")
    private int amount;

    @Column(name="min_price")
    private BigDecimal minPrice;

    @Column(name="date_limit")
    private Date dateLimit;

    public SellOffer() {
    }

    public SellOffer(int id, Stock stock, int amount, BigDecimal minPrice, Date dateLimit) {
        this.id = id;
        this.stock = stock;
        this.amount = amount;
        this.minPrice = minPrice;
        this.dateLimit = dateLimit;
    }

    public SellOffer(SellOfferDTO sellOfferDTO, Stock stock) {
        this.id = sellOfferDTO.getId();
        this.stock = stock;
        this.amount = sellOfferDTO.getAmount();
        this.minPrice = sellOfferDTO.getMinPrice();
        this.dateLimit = sellOfferDTO.getDateLimit();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
    }
}
