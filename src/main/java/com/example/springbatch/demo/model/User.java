package com.example.springbatch.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String Country;
    private String region;
    private String population;
    private String totalUsers;
    private double percentage;

    public User() {
    }

    public User(String country, String region) {
        Country = country;
        this.region = region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
        this.totalUsers = totalUsers;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
