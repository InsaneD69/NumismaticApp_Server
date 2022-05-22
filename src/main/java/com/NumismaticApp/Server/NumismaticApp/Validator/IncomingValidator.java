package com.NumismaticApp.Server.NumismaticApp.Validator;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;

import java.io.IOException;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;

public class IncomingValidator {

    public static void checkExistLanguage(String lang) throws IOException, LanguageNotExistException {


        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);
        boolean status =property.open().getProperty("existLang").contains(lang);
        property.close();

        if (!status){

            throw new LanguageNotExistException("Wrong language");

        }



    }
}
