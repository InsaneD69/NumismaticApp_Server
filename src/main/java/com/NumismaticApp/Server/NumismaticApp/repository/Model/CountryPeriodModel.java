package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.liteCoin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountryPeriodModel implements CountryPeriodInterface {

    private  String country;
    private String namePeriod;       // название периода
    private short bgYear;            // начало периода
    private short endYear;
    private String currenciesAndNominalValues;  // ключи -  номинал и валюта, значение - обозначение в таблице
    private ArrayList<liteCoin> listOnePeriodCountry;

    public String getCurrenciesAndNominalValues() {
        return currenciesAndNominalValues;
    }




    public void setCurrenciesAndNominalValues(Map<String,String> currenciesAndNominalValuesMap)  {
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

    public ArrayList<liteCoin> getListOnePeriodCountry() {
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

    public void setListOnePeriodCountry(ArrayList<liteCoin> listOnePeriodCountry) {
        this.listOnePeriodCountry = listOnePeriodCountry;
    }


    public CountryPeriodModel toModel(CountryPeriod countryPeriod){

        CountryPeriodModel countryPeriodModel = new CountryPeriodModel();

        countryPeriodModel.setCountry(countryPeriod.getCountry());
        countryPeriodModel.setNamePeriod(countryPeriod.getNamePeriod());
        countryPeriodModel.setBgYear(countryPeriod.getBgYear());
        countryPeriodModel.setEndYear(countryPeriod.getEndYear());
        countryPeriodModel.setCurrenciesAndNominalValues(countryPeriod.getCurrenciesAndNominalValues());
        countryPeriodModel.setListOnePeriodCountry(countryPeriod.getListOnePeriodCountry());
        return  countryPeriodModel;


    }

    static public CountryPeriodModel getModel(CountryPeriod countryPeriod){

        CountryPeriodModel countryPeriodModel =new CountryPeriodModel();

        return countryPeriodModel.toModel(countryPeriod);

    }
}
