package com.numismatic_app.server.dto;

import java.io.Serializable;


public class CountryInfoDTO implements Serializable {

    private String country;

    public CountryInfoDTO() {
        //Don't use
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
