package com.NumismaticApp.Server.NumismaticApp.Validator;

import com.NumismaticApp.Server.NumismaticApp.DTO.SearchInformation;
import com.NumismaticApp.Server.NumismaticApp.Exception.CountryNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.*;

public class IncomingSearcherValidator {

    private  ArrayList<String> currenciesAndValues=new ArrayList<>();

    public Optional<ArrayList<String>> getCurrenciesAndValues() {
        return Optional.ofNullable(currenciesAndValues);
    }

    public   Optional<ArrayList<String>> validate(SearchInformation searchInformation) throws IOException, ClassNotFoundException, CountryNotExistException, LanguageNotExistException {

       IncomingCountryValidator.checkExistCountry(searchInformation.getCountry());

        IncomingSearcherValidator val =new IncomingSearcherValidator();

        val.validateCurrenciesAndValues(searchInformation);


       return val.getCurrenciesAndValues();



    }

    public   void validateCurrenciesAndValues(SearchInformation searchInformation){

        ArrayList<String> values = searchInformation.getValue().orElse(new ArrayList<>(Arrays.asList("")));
        ArrayList<String> currencies = searchInformation.getCurrency().orElse(new ArrayList<>(Arrays.asList("")));


         values.forEach(value->{
            currencies.forEach(currency-> {
                        currenciesAndValues.add(value+" "+currency);
            }
            );
        });

    }



}
