package com.raphau.springboot.stockExchange.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="test")
public class Test implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="database_time")
    private long databaseTime;

    @Column(name = "application_time")
    private long applicationTime;

    public Test() {
    }

    public Test(int id, String name, long databaseTime, long applicationTime) {
        this.id = id;
        this.name = name;
        this.databaseTime = databaseTime;
        this.applicationTime = applicationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDatabaseTime() {
        return databaseTime;
    }

    public void setDatabaseTime(long database_time) {
        this.databaseTime = database_time;
    }

    public long getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(long application_time) {
        this.applicationTime = application_time;
    }
}
