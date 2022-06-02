package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;


import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.SaverParseInfo;
import com.NumismaticApp.Server.NumismaticApp.Exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;


@Log4j2
@Component
public class UCoinComponent implements CommandLineRunner {

    private SaverParseInfo saverParseInfo;

    @Override
    public void run(String... args)  {


         try {
            log.info("CountryListComponent start");


            while (true) {

                necessity();
                try {
                    openStreams();
                } catch (SiteConnectionError e) {
                    log.error(e.getMessage());

                }
                closeStreams();
                waitFor();
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error");
        }
    }

        private void openStreams() throws IOException, InterruptedException, SiteConnectionError {

            PropertyConnection property = new PropertyConnection(pathToUcoinProperty);
        try{

            for(String lang:property.open().getProperty("existLang").split(",")){

             String countryListWay = new File("").getAbsolutePath()+property.open().getProperty("countriesList."+lang);
             saverParseInfo  = new SaverParseInfo(countryListWay);
             saveCountriesIntoFile(lang);
             waitForLittle();

         }
        }
        catch (SiteConnectionError e){

            throw new SiteConnectionError(e.getMessage());

        }
        finally {
            property.close();
        }


    }

    private void necessity() throws IOException, InterruptedException {

       PropertyConnection property = new PropertyConnection(pathToUcoinProperty);
       if(property.open().getProperty("enableLoadingCountriesList").equals("false")){

           log.info("Not necessity to load countries list");
           property.close();
           waitForMiddle();
           necessity();
       }


    }


    private void closeStreams() throws IOException {
       saverParseInfo.close();
    }

    private void saveCountriesIntoFile(String lang) throws IOException, SiteConnectionError {

        ArrayList<Set<String>> countriesBufferStorage = new ArrayList<>(CoinSearcher.getCountriesFromUcoin(lang));

        deleteOldData();

        log.info("Country  list "+lang+" had been downloaded");
        saverParseInfo.save(countriesBufferStorage);
        log.info("Country list "+lang+" had been saved");


    }

    private  void deleteOldData(){


        File countriesInfo = new File(new File("").getAbsolutePath()+"/src/main/resources/SearcherInformation/CountriesInfo");
        String[] data= countriesInfo.list();


        if (data!=null){


            for(String nameFile:data){
                log.info("deleting "+nameFile);
                File deletingFile =new File(countriesInfo.getPath(),nameFile);
                deletingFile.delete();
            }
        log.info("old data successful deleted");

        }


    }

    private void waitFor() throws InterruptedException {
        TimeUnit.HOURS.sleep(1);
    }
    private void waitForLittle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(50);
    }
    private void waitForMiddle() throws InterruptedException {
        TimeUnit.MINUTES.sleep(10);
    }

}




