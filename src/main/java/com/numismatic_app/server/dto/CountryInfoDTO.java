package com.numismatic_app.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;


public class CountryInfoDTO implements Serializable {

    private String country;

    private ArrayList<String> periods;


    public CountryInfoDTO() {
        //Don't use
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public ArrayList<String> getPeriods() {
        ArrayList<String> el = new ArrayList<>();
        return Optional.ofNullable(periods).orElse(el);
    }

    public void setPeriods(ArrayList<String> periods) {
        this.periods = periods;
    }
}
