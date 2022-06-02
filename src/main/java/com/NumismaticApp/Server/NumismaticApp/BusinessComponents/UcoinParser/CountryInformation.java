package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.Exception.SiteConnectionError;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CountryInformation implements Serializable {                           // содержит информацию о стране

    private ArrayList<CountryPeriod> periods;                 // список с периодами страны

    private String nameCountry;


    public CountryInformation(Elements countryPeriods, String country) throws IOException, SiteConnectionError {

        this.nameCountry=country;

        periods = new ArrayList<>();

        countryPeriods.forEach(period->{                   //на каждый период создается свой объект класса Period


            try {
                periods.add(new CountryPeriod(period,country));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        try {
            periods.get(0).setCurrenciesAndNominalValues();

            if (periods.size() > 1) {

                if (periods.get(1) != null) {

                    periods.get(1).setCurrenciesAndNominalValues();

                    if (periods.get(1).getCurrenciesAndNominalValues() == null) {

                        if (periods.size() > 2) {

                            if (periods.get(2) != null) {

                                periods.get(2).setCurrenciesAndNominalValues();

                            }
                        }
                    }
                }
            }
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }


    }
    public List<LiteCoin> getCoinFromPeriod(int requiredYear){




        return periods.get(0).getListOnePeriodCountry()
                .stream()
                .filter(elem->elem.getYear()==requiredYear)
                .collect(Collectors.toList());



    }

    /*public List<CountryPeriod> getPeriodByYear(int requiredYear){


        List<CountryPeriod> requiredPeriods= periods
                .stream()
                .filter(elem->elem.compareData(requiredYear))
                .collect(Collectors.toList());

        requiredPeriods.forEach((period)->{

            try {
                period.setCurrenciesAndNominalValues();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SiteConnectionError e) {
                throw new SiteConnectionError(e.getMessage());
            }

        });


        return requiredPeriods;

    }*/

    private void waitForLittle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
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
    public int hashCode() {
        return nameCountry.hashCode()+periods.hashCode();
    }
}

