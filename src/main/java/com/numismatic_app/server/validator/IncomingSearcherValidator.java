package com.numismatic_app.server.validator;

import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.exception.CountryNotExistException;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;

/**
 * Обрабатывает данные, которые будут использоваться для поиска монеты.
 */
@Log4j2
public class IncomingSearcherValidator {

    /** Склеивает каждый друг с другом  номинал и валютю, которые находятся внтури объекта
     *     класса SearchInformation: номиналы и валюты
     * @param searchInformation Содержит все поля, необходимые для поиска монеты {@link SearchInformation}
     * @return Список из комбинаций номиналов и валют
     */
    public static  ArrayList<String> validateCurrenciesAndValues(SearchInformation searchInformation){
        ArrayList<String> currenciesAndValues=new ArrayList<>();
        ArrayList<String> values = searchInformation.getValue();
        ArrayList<String> currencies = searchInformation.getCurrency();

        log.info(" Get values: "+ values);
        log.info(" Get currencies "+currencies);
        log.info(" Get years "+searchInformation.getYear());
        log.info(" Get periods "+searchInformation.getPeriod());

         values.forEach(oneValue->
             currencies.forEach(oneCurrency->

                 currenciesAndValues.add(
                         Optional.ofNullable(oneValue).orElse("")+
                               " "+
                               Optional.ofNullable(oneCurrency).orElse("")
                 )

             )
        );
        return Optional.ofNullable(currenciesAndValues)
                .orElse(new ArrayList<>(Arrays.asList("")));

    }

    private IncomingSearcherValidator(){}

}
