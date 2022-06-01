package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.LiteCoin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;


public class CountryPeriodModel implements CountryPeriodInterface, Serializable {

    private  String country;
    private String namePeriod;       // название периода
    private short bgYear;            // начало периода
    private short endYear;
    private String currenciesAndNominalValues;  // ключи -  номинал и валюта, значение - обозначение в таблице
    private ArrayList<LiteCoin> listOnePeriodCountry;

    public String getCurrenciesAndNominalValues() {
        return currenciesAndNominalValues;
    }




    public void setCurrenciesAndNominalValues(Map<String,String> currenciesAndNominalValuesMap) throws URISyntaxException {
        //конвертиция из Map в стринг
       ObjectMapper objectMapper = new ObjectMapper();
       String valAndCur;
       try{
         valAndCur=objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(currenciesAndNominalValuesMap);
           this.currenciesAndNominalValues = valAndCur;

         //обратная конвертация
         /*  Map<String, String > we =objectMapper.readValue(valAndCur, new TypeReference<Map<String, String >>(){});

           we.forEach((key,value)->{

               System.out.println(key+"  :  "+value);

           });*/




       }
       catch (JsonProcessingException e)
       {e.printStackTrace();}


    }





    public String getCountry() {
        return country;
    }

    public String getNamePeriod() {
        return namePeriod;
    }

    public short getBgYear() {
        return bgYear;
    }

    public short getEndYear() {
        return endYear;
    }

    public ArrayList<LiteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public void setNamePeriod(String namePeriod) {
        this.namePeriod = namePeriod;
    }

    public void setBgYear(short bgYear) {
        this.bgYear = bgYear;
    }

    public void setEndYear(short endYear) {
        this.endYear = endYear;
    }

    public void setListOnePeriodCountry(ArrayList<LiteCoin> listOnePeriodCountry) {
        this.listOnePeriodCountry = listOnePeriodCountry;
    }


    public CountryPeriodModel toModel(CountryPeriod countryPeriod) throws URISyntaxException {

        CountryPeriodModel countryPeriodModel = new CountryPeriodModel();

        countryPeriodModel.setCountry(countryPeriod.getCountry());
        countryPeriodModel.setNamePeriod(countryPeriod.getNamePeriod());
        countryPeriodModel.setBgYear(countryPeriod.getBgYear());
        countryPeriodModel.setEndYear(countryPeriod.getEndYear());
        countryPeriodModel.setCurrenciesAndNominalValues(countryPeriod.getCurrenciesAndNominalValues());
        countryPeriodModel.setListOnePeriodCountry(countryPeriod.getListOnePeriodCountry());
        return  countryPeriodModel;


    }

    static public CountryPeriodModel getModel(CountryPeriod countryPeriod) throws URISyntaxException {

        CountryPeriodModel countryPeriodModel =new CountryPeriodModel();

        return countryPeriodModel.toModel(countryPeriod);

    }
}
