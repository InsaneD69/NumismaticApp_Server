package com.NumismaticApp.Server.NumismaticApp.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SearchInformation implements Serializable {

    private String country;
    private ArrayList<Integer> year;
    private ArrayList<String> value;
    private ArrayList<String> currency;
    private ArrayList<String> mint;


    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public Optional<ArrayList<Integer>> getYear() {
        return  Optional.ofNullable(year);
    }



    public Optional<ArrayList<String>> getValue() {
        return Optional.ofNullable(value);
    }



    public Optional<ArrayList<String>> getCurrency() {
        return Optional.ofNullable(currency);
    }



    public Optional<ArrayList<String>> getMint() {
        return Optional.ofNullable(mint);
    }

    public void setYear(ArrayList<Integer> year) {
        this.year = year;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public void setCurrency(ArrayList<String> currency) {
        this.currency = currency;
    }

    public void setMint(ArrayList<String> mint) {
        this.mint = mint;
    }

    public SearchInformation(){

   }

}
