package com.NumismaticApp.Server.NumismaticApp.BusinessComponents;


import com.NumismaticApp.Server.NumismaticApp.UcoinParser.CoinSearcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2

public class CountryListComponent implements CommandLineRunner {

    public static String pathToCountriesList=new File("").getAbsolutePath()+"/src/main/resources/SearcherInformation/countryList.txt";
    private ObjectOutputStream saveToFile;

    private FileOutputStream fileStream;

    @Override
    public void run(String... args) throws Exception {

        try {
            openStreams();

            while (true) {
                saveCountriesIntoFile();
                closeStreams();
                waitFor();
                openStreams();
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error");
            return;
        }
    }

    private void openStreams() throws IOException {

        File countryList = new File(pathToCountriesList);

        fileStream = new FileOutputStream(countryList);

        saveToFile = new ObjectOutputStream(fileStream);
    }

    private void closeStreams() throws IOException {
        fileStream.close();
        saveToFile.close();
    }

    private void saveCountriesIntoFile() throws IOException {

        ArrayList<Set<String>> countriesBufferStorage = new ArrayList<>(CoinSearcher.getCountriesFromUcoin());
        log.info("Country list had been downloaded");
        saveToFile.writeObject(countriesBufferStorage);
        saveToFile.flush();
        log.info("Country list had been saved");


    }

    private void waitFor() throws InterruptedException {
        TimeUnit.HOURS.sleep(1);
    }


}




