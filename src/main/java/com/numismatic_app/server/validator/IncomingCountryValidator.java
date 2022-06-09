package com.numismatic_app.server.validator;

import com.numismatic_app.server.file_worker.GetterInfo;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;


/**
 * Проверяет корректность страны на язык написания и грамматические ошибки
 */
@Log4j2
public  class IncomingCountryValidator {


    /**
     *
     */
    private IncomingCountryValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkExistCountry(String country, String lang) throws CountryNotExistException, IOException, ClassNotFoundException {


            PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

            File file = new File(new File("")
                    .getAbsolutePath() +
                    (property.open().getProperty("countriesList." + lang))
            );

            property.close();


            GetterInfo getterInfo = new GetterInfo(file.getAbsolutePath());
            ArrayList<String> listCountries = (ArrayList<String>) getterInfo.get();
            getterInfo.close();

            if (!listCountries.contains(country)) {

                throw new CountryNotExistException("Wrong country");

            }

    }

}
