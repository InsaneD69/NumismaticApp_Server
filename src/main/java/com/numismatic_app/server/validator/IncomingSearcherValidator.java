package com.numismatic_app.server.validator;

import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.exception.LanguageNotExistException;

import java.io.IOException;
import java.util.*;

public class IncomingSearcherValidator {

    private  ArrayList<String> currenciesAndValues=new ArrayList<>();

    public ArrayList<String> getCurrenciesAndValues() {
        return Optional.ofNullable(currenciesAndValues).orElse(new ArrayList<>(Arrays.asList("")));
    }

    public   ArrayList<String> validate(SearchInformation searchInformation) throws IOException, ClassNotFoundException, CountryNotExistException, LanguageNotExistException {

       IncomingCountryValidator.checkExistCountry(searchInformation.getCountry());

        IncomingSearcherValidator val =new IncomingSearcherValidator();

        val.validateCurrenciesAndValues(searchInformation);


       return val.getCurrenciesAndValues();



    }

    public   void validateCurrenciesAndValues(SearchInformation searchInformation){

        ArrayList<String> values = searchInformation.getValue();
        ArrayList<String> currencies = searchInformation.getCurrency();
        ArrayList<Integer> years = searchInformation.getYear();

        System.out.println("values "+values);
        System.out.println("currencies "+currencies);

        System.out.println(values.size());
        System.out.println(currencies.size());
         values.forEach(oneValue->{
            currencies.forEach(oneCurrency-> {

                String fs=  Optional.ofNullable(oneValue).orElse("")+
                        " "+
                        Optional.ofNullable(oneCurrency).orElse("");
                System.out.println("fs "+fs);
                        currenciesAndValues.add(
                                Optional.ofNullable(oneValue).orElse("")+
                                        " "+
                                        Optional.ofNullable(oneCurrency).orElse("")
                        );
                  }
            );
        });

         System.out.println( currenciesAndValues);

    }



}
