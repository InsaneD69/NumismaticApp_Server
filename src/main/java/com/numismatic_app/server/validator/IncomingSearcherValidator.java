package com.numismatic_app.server.validator;

import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.exception.LanguageNotExistException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;

@Log4j2
public class IncomingSearcherValidator {

    private  ArrayList<String> currenciesAndValues=new ArrayList<>();

    public ArrayList<String> getCurrenciesAndValues() {
        return Optional.ofNullable(currenciesAndValues).orElse(new ArrayList<>(Arrays.asList("")));
    }

    public   ArrayList<String> validate(SearchInformation searchInformation, String lang) throws CountryNotExistException, ServerException {

        try {

            IncomingCountryValidator.checkExistCountry(searchInformation.getCountry(), lang);

            IncomingSearcherValidator val = new IncomingSearcherValidator();

            val.validateCurrenciesAndValues(searchInformation);


            return val.getCurrenciesAndValues();

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



}
