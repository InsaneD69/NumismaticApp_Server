package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;

import java.io.Serializable;

public class CountryPeriodDemoModel implements CountryPeriodInterface {

    private  String country;
    private String namePeriod;       // название периода
    private short bgYear;            // начало периода
    private short endYear;

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

    public CountryPeriodDemoModel toModel(CountryPeriod countryPeriod){


        CountryPeriodDemoModel countryPeriodDemoModel = new CountryPeriodDemoModel();

        countryPeriodDemoModel.setCountry(countryPeriod.getCountry());
        countryPeriodDemoModel.setNamePeriod(countryPeriod.getNamePeriod());
        countryPeriodDemoModel.setBgYear(countryPeriod.getBgYear());
        countryPeriodDemoModel.setEndYear(countryPeriod.getEndYear());
        return countryPeriodDemoModel;

    }

    static public CountryPeriodDemoModel getModel(CountryPeriod countryPeriod){

        CountryPeriodDemoModel countryPeriodDemoModel =new CountryPeriodDemoModel();

       return countryPeriodDemoModel.toModel(countryPeriod);

    }
}
