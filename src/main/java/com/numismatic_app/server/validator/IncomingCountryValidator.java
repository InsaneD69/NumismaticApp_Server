package com.numismatic_app.server.validator;

import com.numismatic_app.server.file_worker.GetterInfo;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

@Log4j2
public class IncomingCountryValidator {

    public static void checkExistCountry(String country) throws  CountryNotExistException {

        try {
            PropertyConnection property = new PropertyConnection(CoinSearcher.pathToUcoinProperty);

            File file = new File(new File("").getAbsolutePath() +
                    (property.open()
                            .getProperty("countriesList."
                                    + Thread.currentThread().getName()))
            );

            property.close();


            GetterInfo getterInfo = new GetterInfo(file.getAbsolutePath());
            ArrayList<Set<String>> listCountries = (ArrayList<Set<String>>) getterInfo.get();
            getterInfo.close();


            boolean status = listCountries.contains(country);

            if (!status) {

                throw new CountryNotExistException("Wrong country");

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


}
