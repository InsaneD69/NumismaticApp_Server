package com.numismatic_app.server.dto;

import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryPeriod;


import java.io.Serializable;
import java.util.*;


public class CountryDenominationInfo implements Serializable {
    private String country;
    private HashSet<ValAndCurPair> curAndValues;
    private boolean allInfo;

    private String countryYearsPeriod;

    public String getCountryYearsPeriod() {
        return countryYearsPeriod;
    }

    public void setCountryYearsPeriod(String countryYearsPeriod) {
        this.countryYearsPeriod = countryYearsPeriod;
    }

    public HashSet<ValAndCurPair> getCurAndValues() {
        return curAndValues;
    }

    public void setCurAndValues(HashSet<ValAndCurPair> curAndValues) {
        this.curAndValues = curAndValues;
    }

    public CountryDenominationInfo() {
        //Do nothing
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

        denominationInfo.setCountryYearsPeriod(countryInformation.getCountryYearsPeriod());

        denominationInfo.setAllInfo(true);

        HashSet<ValAndCurPair> arPair = new HashSet<>();

        for(CountryPeriod period: countryInformation.getPeriods()){

              Map<String,String> map=period.getCurrenciesAndNominalValues();

              if(map!=null){

                  map.forEach((key,value)->{

                   String[] part= value.split(" ",2);
                   arPair.add(new ValAndCurPair(part[0],part[1]));

                  });

              }else{

                  denominationInfo.setAllInfo(false);

              }

        }

        denominationInfo.setCurAndValues(arPair);

        return denominationInfo;


    }

    @Override
    public String toString() {
        return "CountryDenominationInfo{" +
                "country='" + country + '\'' +
                //", curAndValues=" + curAndValues +
                ", allInfo=" + allInfo +
                '}';
    }
}
