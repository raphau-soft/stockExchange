package com.raphau.springboot.stockExchange.entity;

import javax.persistence.*;

@Entity
@Table(name="Company", schema = "stock_exchange")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

}
