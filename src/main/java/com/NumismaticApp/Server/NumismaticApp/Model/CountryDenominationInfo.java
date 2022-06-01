package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CountryDenominationInfo {
    private String country;
    private Map<String,String> curAndValues;
    private boolean allInfo;

    public CountryDenominationInfo() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, String> getCurAndValues() {
        return curAndValues;
    }

    public void setCurAndValues() {

        this.curAndValues = new HashMap<>();
    }
    public void addToCurAndValues(String f, String s){

        curAndValues.put(f,s);

    }

    public boolean isAllInfo() {
        return allInfo;
    }

    public void setAllInfo(boolean allInfo) {
        this.allInfo = allInfo;
    }


    public static CountryDenominationInfo toModel(CountryInformation countryInformation){

        CountryDenominationInfo denominationInfo = new CountryDenominationInfo();


        denominationInfo.setCountry(countryInformation.getNameCountry());

        denominationInfo.setCurAndValues();

        denominationInfo.setAllInfo(true);

        for(CountryPeriod period: countryInformation.getPeriods()){

              Map<String,String> map=period.getCurrenciesAndNominalValues();

              if(map!=null){

                  map.forEach((key,value)->{

                     String [] parts = value.split(" ",2);

                     denominationInfo.addToCurAndValues(parts[0],parts[1]);

                  });

              }else{

                  denominationInfo.setAllInfo(false);
                  break;

              }

        }

        return denominationInfo;


    }
}
