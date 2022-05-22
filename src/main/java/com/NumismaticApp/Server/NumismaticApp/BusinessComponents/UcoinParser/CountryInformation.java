package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CountryInformation {                           // содержит информацию о стране

    public ArrayList<CountryPeriod> periods;                 // список с периодами страны

    public CountryInformation(Elements countryPeriods) throws IOException {

        periods = new ArrayList<>();

        countryPeriods.forEach(period->{                   //на каждый период создается свой объект класса Period

            try {
                periods.add(new CountryPeriod(period));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        periods.get(0).setCurrenciesAndNominalValues(); //получает список валют самого современного периода страны

        System.out.println(periods.get(0).getNominalValues());
        System.out.println(periods.get(0).getCurrencies());



    }
    public List<liteCoin> getCoinFromPeriod(int requiredYear){




        return periods.get(0).getListOnePeriodCountry()
                .stream()
                .filter(elem->elem.getYear()==requiredYear)
                .collect(Collectors.toList());



    }

}

