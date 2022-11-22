package com.numismatic_app.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class SearchInformation implements Serializable {

    private String country;
    private ArrayList<Integer> year;
    private ArrayList<String> value;
    private ArrayList<String> currency;
    private ArrayList<String> period;


    public ArrayList<String> getPeriod() {

        ArrayList<String> el= new ArrayList<>();
        el.add("none");
        return  Optional.ofNullable(period).orElse(el);
    }

    public void setPeriod(ArrayList<String> period) {
        this.period = period;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<Integer> getYear() {

        ArrayList<Integer> el= new ArrayList<>();
        el.add(99999);
        return  Optional.ofNullable(year).orElse(el);
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





    public void setYear(ArrayList<Integer> year) {
        this.year = year;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public void setCurrency(ArrayList<String> currency) {
        this.currency = currency;
    }



    public SearchInformation(){
        //Do nothing
   }



    @Override
    public String toString() {
        return "SearchInformation{" +
                "country='" + country + '\'' +
                ", year=" + year +
                ", period=" + period +
                ", value=" + value +
                ", currency=" + currency +
                '}';
    }
}
