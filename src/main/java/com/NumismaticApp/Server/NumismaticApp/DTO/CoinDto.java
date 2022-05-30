package com.NumismaticApp.Server.NumismaticApp.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


public class CoinDto implements Serializable {


    private String country;
    private String currency;
    private Integer years;
    private String cost;
    private String linkUcoin;
    private LocalDate dataOfCreate;
    private String value;
    private String mint;

    private LinkedHashMap<String,String> infoTable;

    public CoinDto() {


    }

    public CoinDto(String country, String currency, Integer years, String cost, String linkUcoin, LocalDate dataOfCreate, String value, String mint) {
        this.country = country;
        this.currency = currency;
        this.years = years;
        this.cost = cost;
        this.linkUcoin = linkUcoin;
        this.dataOfCreate = LocalDate.now();
        this.value = value;
        this.mint=mint;
    }

    public Map<String, String> getInfoTable() {
        return infoTable;
    }

    public void setInfoTable() {
        this.infoTable=new LinkedHashMap<>();
    }

    public void addToInfoTable(String firstElem, String secondElem) {

        this.infoTable.put(firstElem,secondElem);
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public  Integer getYears() {
        return years;
    }
    public void setYears( Integer years) {
        this.years = years;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getLinkUcoin() {
        return linkUcoin;
    }
    public void setLinkUcoin(String linkUcoin) {
        this.linkUcoin = linkUcoin;
    }
    public LocalDate getDataOfCreate() {
        return dataOfCreate;
    }
    public void setDataOfCreate(LocalDate dataOfCreate) {
        this.dataOfCreate = dataOfCreate;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getMint() {
        return mint;
    }
    public void setMint(String mint) {
        this.mint = mint;
    }

    @Override
    public String toString() {

        return "Монета"+'\''+
                "Страна: "+getCountry()+'\''+
                "Валюта: "+getCurrency()+'\''+
                "Номинал: "+getValue()+'\''+
                "Год: "+ getYears()+'\''+
                "Монетный двор: "+getMint()+'\''+
                "Примерная стоимость: "+getCost()+'\''+
                "ссылка: "+getLinkUcoin()+'\''+
                "Дата создания: "+getDataOfCreate()+'\''+
                "Таблица с данными: \n"+getInfoTable()
                ;


    }
}
