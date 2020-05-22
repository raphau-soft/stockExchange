package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(targetEntity = BuyOffer.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="buy_offer_id", nullable = false)
    private BuyOffer buyOffer;

    @ManyToOne(targetEntity = SellOffer.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="sell_offer_id", nullable = false)
    private SellOffer sellOffer;

    @Column(name="amount")
    private int amount;

    @Column(name = "price")
    private double price;

    @Column(name = "transaction_date")
    private Date date;
}
