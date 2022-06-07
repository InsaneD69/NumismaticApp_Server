package com.numismatic_app.server.business_components.ucoin_parser;

import com.numismatic_app.server.exception.SiteConnectionError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CoinSearcherTest {

    @Test
    @DisplayName("Получает список стран с сайта ucoin на русском языке")
    void testFuncGetCountriesOnRussianLang() throws SiteConnectionError, IOException {

        ArrayList<String> countryList=CoinSearcher.getCountriesFromUcoin("ru");
        String expected = "Россия";
        System.out.println(countryList);
        Assertions.assertTrue(countryList.contains(expected),"список стран не был получен на русском языке или вообще");
    }
    @Test
    @DisplayName("Получает список стран с сайта ucoin на английском языке")
    void testFuncGetCountriesOnEnglishLang() throws SiteConnectionError, IOException {

        ArrayList<String> countryList=CoinSearcher.getCountriesFromUcoin("en");
        String expected = "Russia";
        System.out.println(countryList);
        Assertions.assertTrue(countryList.contains(expected),"список стран не был получен на английском языке или вообще");
    }


}