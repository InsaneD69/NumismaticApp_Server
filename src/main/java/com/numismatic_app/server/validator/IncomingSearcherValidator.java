package com.numismatic_app.server.validator;

import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.exception.CountryNotExistException;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;

@Log4j2
public class IncomingSearcherValidator {



    private String lang;
    private  ArrayList<String> currenciesAndValues=new ArrayList<>();

    public IncomingSearcherValidator(String lang) {
        this.lang=lang;
    }

    public   ArrayList<String> validate(SearchInformation searchInformation) throws CountryNotExistException, ServerException {

        try {

            IncomingCountryValidator.checkExistCountry(searchInformation.getCountry(), lang);


            validateCurrenciesAndValues(searchInformation);


            return getCurrenciesAndValues();

        }  catch (CountryNotExistException e) {
           throw new CountryNotExistException(e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            throw new ServerException("Server Error");
        }


    }

    public   void validateCurrenciesAndValues(SearchInformation searchInformation){

        ArrayList<String> values = searchInformation.getValue();
        ArrayList<String> currencies = searchInformation.getCurrency();


        log.info(" Get values: "+ values);
        log.info(" Get currencies "+currencies);


         values.forEach(oneValue->
             currencies.forEach(oneCurrency->

                 currenciesAndValues.add(
                         Optional.ofNullable(oneValue).orElse("")+
                               " "+
                               Optional.ofNullable(oneCurrency).orElse("")
                 )

             )
        );


    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ArrayList<String> getCurrenciesAndValues() {
        return Optional.ofNullable(currenciesAndValues).orElse(new ArrayList<>(Arrays.asList("")));
    }

    public void setCurrenciesAndValues(ArrayList<String> currenciesAndValues) {
        this.currenciesAndValues = currenciesAndValues;
    }
}
