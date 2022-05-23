package com.NumismaticApp.Server.NumismaticApp.Service;


import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;

@Service
@Log4j2
public class ParseService {


    @Autowired
    private void ParseService() throws IOException {
    }


    public ArrayList<String> getCountryList() throws IOException, ClassNotFoundException, InterruptedException {
        waitForLittle();
        log.info("taken list of country: given to "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());
        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);

        File list = new File(new File("").getAbsolutePath()+property.open().getProperty("countriesList."+Thread.currentThread().getName()));
        FileInputStream fileInputStream = new FileInputStream(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ArrayList<String> countryList =(ArrayList<String>) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();


        return countryList;


    }
    public  CountryInformation getInfoAboutCountry(String country) throws IOException, ClassNotFoundException, InterruptedException {

         return  CoinSearcher.getInfoAboutCountry(country);

    }




    private void waitForLittle() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }


}
