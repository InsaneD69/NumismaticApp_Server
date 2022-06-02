package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.ValAndCurPair;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.data.util.Pair;


import java.io.Serializable;
import java.security.KeyPair;
import java.util.*;


public class CountryDenominationInfo implements Serializable {
    private String country;
    private HashSet<ValAndCurPair> curAndValues;
    private boolean allInfo;



    public HashSet<ValAndCurPair> getCurAndValues() {
        return curAndValues;
    }

    public void setCurAndValues(HashSet<ValAndCurPair> curAndValues) {
        this.curAndValues = curAndValues;
    }

    public CountryDenominationInfo() {
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



        denominationInfo.setAllInfo(true);

        HashSet<ValAndCurPair> arPair = new HashSet<>();

        for(CountryPeriod period: countryInformation.getPeriods()){

              Map<String,String> map=period.getCurrenciesAndNominalValues();

              if(map!=null){

                  map.forEach((key,value)->{

                   String[] part= value.split(" ",2);

                   System.out.println(part[0]+" : "+part[1]);

                     arPair.add(new ValAndCurPair(part[0],part[1]));

                  });



              }else{

                  denominationInfo.setAllInfo(false);


              }

        }

        denominationInfo.setCurAndValues(arPair);

        return denominationInfo;


    }
}
