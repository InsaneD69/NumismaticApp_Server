package com.numismatic_app.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
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

    public ArrayList<Integer> getYear() {
        return  Optional.ofNullable(year).orElse(new ArrayList<>(1));
    }



    public ArrayList<String> getValue() {
        ArrayList<String> el = new ArrayList<>();
        el.add("");
        return Optional.ofNullable(value).orElse(el);
    }



    public ArrayList<String> getCurrency() {
        ArrayList<String> el = new ArrayList<>();
        el.add("");
        return Optional.ofNullable(currency).orElse(el);
    }



    public ArrayList<String> getMint() {

        ArrayList<String> el = new ArrayList<>();
        el.add("");
        return Optional.ofNullable(mint).orElse(el);
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



    @Override
    public String toString() {
        return "SearchInformation{" +
                "country='" + country + '\'' +
                ", year=" + year +
                ", value=" + value +
                ", currency=" + currency +
                ", mint=" + mint +
                '}';
    }
}
