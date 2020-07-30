package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="buy_offer", schema = "stock_exchange")
public class BuyOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = Company.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "buyOffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Transaction> transactions;

    @Column(name="max_price")
    private BigDecimal maxPrice;

    @Column(name="amount")
    private int amount;

    @Column(name="date_limit")
    private Date dateLimit;

    public BuyOffer() {
    }

    public BuyOffer(int id, Company company, User user, BigDecimal maxPrice, int amount, Date dateLimit) {
        this.id = id;
        this.company = company;
        this.user = user;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        return "BuyOffer{" +
                "id=" + id +
                ", company=" + company +
                ", user=" + user +
                ", maxPrice=" + maxPrice +
                ", amount=" + amount +
                ", dateLimit=" + dateLimit +
                '}';
    }
}
