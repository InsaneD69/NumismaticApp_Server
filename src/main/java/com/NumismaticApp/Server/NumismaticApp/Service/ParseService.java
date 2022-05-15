package com.NumismaticApp.Server.NumismaticApp.Service;


import com.NumismaticApp.Server.NumismaticApp.UcoinParser.CoinSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.CountryListComponent.pathToCountriesList;

@Service
public class ParseService {


    @Autowired
    private void ParseService() throws IOException {
    }


    public ArrayList<String> getCountryList() throws IOException, ClassNotFoundException {

        File list = new File(pathToCountriesList);
        FileInputStream fileInputStream = new FileInputStream(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ArrayList<String> countryList =(ArrayList<String>) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();


        return countryList;


    }



}
