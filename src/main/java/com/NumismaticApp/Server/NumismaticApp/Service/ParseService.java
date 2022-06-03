package com.NumismaticApp.Server.NumismaticApp.Service;


import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.GetParseInfo;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.SaverParseInfo;
import com.NumismaticApp.Server.NumismaticApp.DTO.CoinDto;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
        GetParseInfo getParseInfo = new GetParseInfo(new File("").getAbsolutePath()+property.open().getProperty("countriesList."+Thread.currentThread().getName()));

        property.close();

        ArrayList<String> countryList =(ArrayList<String>) getParseInfo.get();

       getParseInfo.close();

        return countryList;

    }
    public  CountryInformation getInfoAboutCountry(String country) throws SiteConnectionError {

        try {
            return  CoinSearcher.getInfoAboutCountry(country);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }

    }

    public Object getRequiredCoins(String country, ArrayList<Integer> year, ArrayList<String> curAndVal ) throws IOException, ClassNotFoundException, SiteConnectionError {


        log.info("curAndVal: "+curAndVal);
        ArrayList<CoinDto> coinDtos = null;
        try {
            coinDtos = CoinSearcher.getCoin(country,year,curAndVal);
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }

        if(coinDtos.isEmpty()){


            return "coins with the specified parameters were not found";

        }
        else  return  coinDtos;

    }



    private void waitForLittle() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }


}
