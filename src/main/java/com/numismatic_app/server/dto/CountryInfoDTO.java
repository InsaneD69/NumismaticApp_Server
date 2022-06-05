package com.numismatic_app.server.dto;

import java.io.Serializable;
import java.util.Optional;

public class CountryInfoDTO implements Serializable {

    private String country;
    private int degree;

    public CountryInfoDTO() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = Optional.ofNullable(degree).orElse(0);
    }
}
