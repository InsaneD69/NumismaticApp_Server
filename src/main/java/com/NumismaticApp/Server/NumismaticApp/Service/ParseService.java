package com.NumismaticApp.Server.NumismaticApp.Service;


import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;

@Service
public class ParseService {


    @Autowired
    private void ParseService() throws IOException {
    }


    public ArrayList<String> getCountryList(String lang) throws IOException, ClassNotFoundException {

        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);

        File list = new File(new File("").getAbsolutePath()+property.open().getProperty("countriesList"+lang));
        FileInputStream fileInputStream = new FileInputStream(list);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        ArrayList<String> countryList =(ArrayList<String>) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();


        return countryList;


    }

   public void checkExistLanguage(String lang) throws IOException, LanguageNotExistException {

        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);
        boolean status =property.open().getProperty("existLang").contains(lang);
        property.close();

        if (!status){

            throw new LanguageNotExistException("Wrong language");

        }



   }


}
