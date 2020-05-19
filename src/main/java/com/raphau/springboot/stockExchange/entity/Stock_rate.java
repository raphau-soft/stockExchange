package com.raphau.springboot.stockExchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="stock_rate")
public class Stock_rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    // company
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Company company;

    @Column(name="rate")
    private double rate;

    @Column(name="date_inc")
    private Date date;

    @Column(name="actual")
    private boolean actual;

    public Stock_rate() {
    }

    public Stock_rate(int id, Company company, double rate, Date date, boolean actual) {
        this.id = id;
        this.company = company;
        this.rate = rate;
        this.date = date;
        this.actual = actual;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }
}
