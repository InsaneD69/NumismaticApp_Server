package com.numismatic_app.server.component;


import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.SiteConnectionError;
import com.numismatic_app.server.file_worker.SaverInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Log4j2
@Component
public class UCoinComponent implements CommandLineRunner {

    private SaverInfo saverParseInfo;



    @Override
    public void run(String... args)  {

         try {
            log.info("CountryListComponent start");


            while (true) {

                necessity();
                openStreams();
                closeStreams();
                waitFor();

            }

        }  catch (SiteConnectionError e) {
             log.error(e.getMessage());

         }
         catch (IOException | InterruptedException e) {
             Thread.currentThread().interrupt();
            log.error(e.getMessage());
        }
    }

        private void openStreams() throws IOException, InterruptedException, SiteConnectionError {

            PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);
        try{

            for(String lang:property.open().getProperty("existLang").split(",")){

                String countryListWay = property.open().getProperty("countriesList."+lang);
                saverParseInfo  = new SaverInfo(countryListWay);
                saveCountriesIntoFile(lang);
                waitForLittle();

             }
            deleteOldData();

        }
        catch (SiteConnectionError e){

            throw new SiteConnectionError(e.getMessage());

        }
        finally {
            property.close();
        }


    }

    private void necessity() throws IOException, InterruptedException {

       PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);
       if(property.open().getProperty("enableLoadingCountriesList").equals("false")){

           log.info("Not necessity to load countries list");
           property.close();
           waitForMiddle();
           necessity();
       }
        property.close();
    }


    private void closeStreams() throws IOException {
       saverParseInfo.close();
    }

    private void saveCountriesIntoFile(String lang) throws IOException, SiteConnectionError {

        ArrayList<String> countriesBufferStorage = new ArrayList<>(CoinSearcher.getCountriesFromUcoin(lang));


        log.info("Country  list "+lang+" had been downloaded");
        saverParseInfo.save(countriesBufferStorage);
        log.info("Country list "+lang+" had been saved");


    }

    private  void deleteOldData() throws IOException {


        File countriesInfo = new File(CoinSearcher.PATH_TO_RESOURCES+"/SearcherInformation/CountriesInfo");
        String[] data= countriesInfo.list();


        if (data!=null){


            for(String nameFile:data){
                log.info("deleting "+nameFile);
                File deletingFile =new File(countriesInfo.getPath(),nameFile);
                if(!Files.deleteIfExists(deletingFile.toPath())){log.info("failing deleting "+nameFile);}
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




