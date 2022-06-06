package com.numismatic_app.server.service;


import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.file_worker.GetterInfo;
import com.numismatic_app.server.dto.CoinDto;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class ParseService {


    @Autowired
    private void ParseService() throws IOException {
    }


    public ArrayList<String> getCountryList() throws IOException, ClassNotFoundException {

        log.info("taken list of country: given to "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());

        PropertyConnection property=new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);
        GetterInfo getParseInfo = new GetterInfo(new File("").getAbsolutePath()+property.open().getProperty("countriesList."+Thread.currentThread().getName()));

        property.close();

        ArrayList<String> countryList =(ArrayList<String>) getParseInfo.get();

       getParseInfo.close();

        log.info("sending countryList"+Thread.currentThread().getName()+" starting with\n"+countryList.get(0));
        return countryList;

    }
    public CountryInformation getInfoAboutCountry(String country) throws SiteConnectionError {

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



    public String getActualCoinCost(String partOfCoin_url) throws IOException, SiteConnectionError {

        PropertyConnection prop = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        String coin_url = prop.open().getProperty("link."+Thread.currentThread().getName())+"/"+partOfCoin_url;

        return   CoinSearcher.getCoinCostFromUcoin(coin_url);

    }

    private void waitForLittle() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }


}
