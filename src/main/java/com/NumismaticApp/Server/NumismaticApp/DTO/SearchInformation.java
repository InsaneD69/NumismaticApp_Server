package com.NumismaticApp.Server.NumismaticApp.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class SearchInformation implements Serializable {

    private String country;
    private  ArrayList<Integer> year;
    private ArrayList<String> value;
    private ArrayList<String> currency;
    private ArrayList<String> mint;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<Integer> getYear() {
        return year;
    }

    public void setYear(ArrayList<Integer> year) {
        this.year = year;
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public ArrayList<String> getCurrency() {
        return currency;
    }

    public void setCurrency(ArrayList<String> currency) {
        this.currency = currency;
    }

    public ArrayList<String> getMint() {
        return mint;
    }

    public void setMint(ArrayList<String> mint) {
        this.mint = mint;
    }

    public SearchInformation(){

   }

}
