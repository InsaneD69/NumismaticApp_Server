package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CountryInformation implements Serializable {                           // содержит информацию о стране

    public ArrayList<CountryPeriod> periods;                 // список с периодами страны


    public CountryInformation(Elements countryPeriods, String country) throws IOException {

        periods = new ArrayList<>();

        countryPeriods.forEach(period->{                   //на каждый период создается свой объект класса Period


            try {
                periods.add(new CountryPeriod(period,country));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        /*periods.forEach(period->{

            try {
                period.setCurrenciesAndNominalValues(); //получает список каждого периода страны
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
       );*/
        periods.get(0).setCurrenciesAndNominalValues();
        if(periods.get(1)!=null){
        periods.get(1).setCurrenciesAndNominalValues();
        }



    }
    public List<liteCoin> getCoinFromPeriod(int requiredYear){




        return periods.get(0).getListOnePeriodCountry()
                .stream()
                .filter(elem->elem.getYear()==requiredYear)
                .collect(Collectors.toList());



    }

    public List<CountryPeriod> getPeriodByYear(int requiredYear){


        List<CountryPeriod> requiredPeriods= periods
                .stream()
                .filter(elem->elem.compareData(requiredYear))
                .collect(Collectors.toList());

        requiredPeriods.forEach((period)->{

            try {
                period.setCurrenciesAndNominalValues();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


        return requiredPeriods;

    }

    private void waitForLittle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

}

