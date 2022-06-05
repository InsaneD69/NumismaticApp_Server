package com.numismatic_app.server.validator;

import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.LanguageNotExistException;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;

import java.io.IOException;

public class IncomingLangValidator {

    public static void checkExistLanguage(String lang) throws LanguageNotExistException {


        try {
            PropertyConnection property = new PropertyConnection(CoinSearcher.pathToUcoinProperty);
            boolean status = property.open().getProperty("existLang").contains(lang);
            property.close();

            if (!status) {

                throw new LanguageNotExistException("Wrong language");

            }
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
