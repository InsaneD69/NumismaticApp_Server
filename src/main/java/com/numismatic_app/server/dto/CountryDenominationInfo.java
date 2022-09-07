package com.numismatic_app.server.dto;

import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryPeriod;


import java.awt.image.AreaAveragingScaleFilter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;


public class CountryDenominationInfo implements Serializable {
    private String country;
    private HashSet<ValAndCurPair> curAndValues;
    private boolean allInfo;

    private String countryYearsPeriod;

    private ArrayList<String> periodsList;

    public ArrayList<String> getPeriodsList() {
        return periodsList;
    }

    public void setPeriodsList(ArrayList<String> periodsList) {
        this.periodsList = periodsList;
    }

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


    public static CountryDenominationInfo toModel(CountryInformation countryInformation, ArrayList<String> certainPeriods){



        CountryDenominationInfo denominationInfo = new CountryDenominationInfo();

        denominationInfo.setCountry(countryInformation.getNameCountry());

        denominationInfo.setCountryYearsPeriod(countryInformation.getCountryYearsPeriod());

        denominationInfo.setAllInfo(true);


        HashSet<ValAndCurPair> arPair = new HashSet<>();



        if (certainPeriods.isEmpty()){

            System.out.println("none");

            denominationInfo=scanAllPeriods(arPair,countryInformation.getPeriods(), denominationInfo);
        }
        else  {
            System.out.println("ne none");denominationInfo=scanCertainPeriods(arPair, countryInformation.getPeriods(), denominationInfo, certainPeriods);}





        return denominationInfo;


    }
    private static CountryDenominationInfo scanAllPeriods(HashSet<ValAndCurPair> arPair,ArrayList<CountryPeriod> countryPeriods, CountryDenominationInfo denominationInfo){

        ArrayList<String> periodCurList = new ArrayList<>();
        for(CountryPeriod period: countryPeriods){

            periodCurList.add(period.getNamePeriod()+" ("+period.getBgYear()+"-"+period.getEndYear()+")");

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
        denominationInfo.setPeriodsList(periodCurList);
        denominationInfo.setCurAndValues(arPair);

        return  denominationInfo;

    }

    private static CountryDenominationInfo scanCertainPeriods( HashSet<ValAndCurPair> arPair ,ArrayList<CountryPeriod> countryPeriods, CountryDenominationInfo denominationInfo,ArrayList<String> certainPeriods){

            for (CountryPeriod period : countryPeriods) {

                for(int a=0;a<certainPeriods.size();a++) {

                    if(certainPeriods.get(a).split(" \\("+period.getBgYear())[0].equals(period.getNamePeriod())) {

                        Map<String, String> map = period.getCurrenciesAndNominalValues();

                        if (map != null) {

                            map.forEach((key, value) -> {

                                String[] part = value.split(" ", 2);
                                arPair.add(new ValAndCurPair(part[0], part[1]));

                            });

                        } else {

                            denominationInfo.setAllInfo(false);

                        }
                    }
                }


        }
        denominationInfo.setCurAndValues(arPair);

        return  denominationInfo;


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
