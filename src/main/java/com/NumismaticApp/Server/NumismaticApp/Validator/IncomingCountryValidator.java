package com.NumismaticApp.Server.NumismaticApp.Validator;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.Exception.CountryNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;
@Log4j2
public class IncomingCountryValidator {

    public static void checkExistCountry(String country) throws IOException, ClassNotFoundException, CountryNotExistException {

        log.info("Checking "+country);
        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);

        File file =new File( new File("").getAbsolutePath()+
                (property.open()
                        .getProperty("countriesList."
                +Thread.currentThread().getName()))
        );
        log.info("Way to check"+file.getName());
        property.close();
        FileInputStream fileInputStream = new FileInputStream(file);

        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ArrayList<Set<String>> listCountries= (ArrayList<Set<String>>) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();

        boolean status= listCountries.contains(country);

        if (!status){

            throw new CountryNotExistException("Wrong country");

        }



    }


}
