package com.NumismaticApp.Server.NumismaticApp.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

public class SearchInformation implements Serializable {

    private String country;
    private Integer year;
    private String value;
    private String currency;
    private String mint;

    public String getCountry() {
        return country;
    }

    public Integer getYear() {
        return year;
    }

    public String getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMint() {
        return mint;
    }

    public SearchInformation(){

   }

}
