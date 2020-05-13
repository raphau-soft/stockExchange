package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Company", schema = "stock_exchange")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @JsonManagedReference
    @Transient
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BuyOffer> buyOffers;

    public Company() {
    }

    public Company(int id, String name, List<BuyOffer> buyOffers) {
        this.id = id;
        this.name = name;
        this.buyOffers = buyOffers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
