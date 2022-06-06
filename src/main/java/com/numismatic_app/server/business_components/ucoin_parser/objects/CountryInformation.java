package com.numismatic_app.server.business_components.ucoin_parser.objects;

import com.numismatic_app.server.exception.SiteConnectionError;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CountryInformation implements Serializable {                           // содержит информацию о стране

    private ArrayList<CountryPeriod> periods;                 // список с периодами страны

    private String nameCountry;


    public CountryInformation(Elements countryPeriods, String country) throws IOException, SiteConnectionError {

        this.nameCountry=country;

        periods = new ArrayList<>();

        countryPeriods.forEach(period->                  //на каждый период создается свой объект класса Period

                periods.add(new CountryPeriod(period,country))

        );

        try {
            int requiredNumOfPeriodsInfo=3;
            for(CountryPeriod period:  periods){

                if(requiredNumOfPeriodsInfo==0) break;
                period.setCurrenciesAndNominalValues();
                requiredNumOfPeriodsInfo--;

            }

        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }

    }

    public List<LiteCoin> getCoinFromPeriod(int requiredYear){


        return periods.get(0).getListOnePeriodCountry()
                .stream()
                .filter(elem->elem.getYear()==requiredYear)
                .toList();

    }

    public String getNameCountry() {
        return nameCountry;
    }

    public ArrayList<CountryPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(ArrayList<CountryPeriod> periods) {
        this.periods = periods;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return nameCountry.hashCode()+periods.hashCode();
    }
}

