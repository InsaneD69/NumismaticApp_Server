package com.numismatic_app.server.validator;

import com.numismatic_app.server.controller.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.LanguageNotExistException;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;

import java.io.IOException;

/**
 * Проверяет корректность аббревиатуры языка
 */
public class IncomingLangValidator {

   private IncomingLangValidator() {
       throw new IllegalStateException("Utility class");
    }
   public static void checkExistLanguage(String lang) throws LanguageNotExistException, IOException {

         PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);
         if (!property.open().getProperty("existLang").contains(lang)) {

             property.close();
             throw new LanguageNotExistException("Wrong language");

         }

         property.close();

   }
}
