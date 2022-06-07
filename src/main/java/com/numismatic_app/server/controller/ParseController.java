package com.numismatic_app.server.controller;

import com.numismatic_app.server.dto.CountryInfoDTO;
import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.dto.CountryDenominationInfo;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.exception.LanguageNotExistException;
import com.numismatic_app.server.exception.ServerWorkException;
import com.numismatic_app.server.service.ParseService;
import com.numismatic_app.server.validator.IncomingCountryValidator;
import com.numismatic_app.server.validator.IncomingDegreeValidator;
import com.numismatic_app.server.validator.IncomingSearcherValidator;
import com.numismatic_app.server.validator.IncomingLangValidator;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;



@RestController
@RequestMapping("/search")
@Log4j2
public class ParseController {

    private static final String ERROR = "Ошибка сервера";

    @Autowired
    private ParseService parseService;


    @GetMapping("/countries")
    public ResponseEntity<Object> getCountries(@RequestParam String lang)  {

        try {
            IncomingLangValidator.checkExistLanguage(lang);

            Thread.currentThread().setName(lang);
            log.info("taken Get request /search/countries: given to thread  "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());
            return ResponseEntity.ok().body(parseService.getCountryList());
        }
        catch (LanguageNotExistException e){

           return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IOException |ClassNotFoundException e) {

            return ResponseEntity.status(500).body(ERROR);

        }



    }

    @PutMapping("/info")
    public ResponseEntity<Object> parseIncomingInfo(@RequestBody CountryInfoDTO countryInfoDTO, @RequestParam String lang)  {

        try{
                IncomingLangValidator
                        .checkExistLanguage(lang);

                IncomingCountryValidator
                        .checkExistCountry(countryInfoDTO.getCountry(),lang);

                IncomingDegreeValidator
                    .checkDegree(countryInfoDTO.getDegree());



            log.info("taken Get request /search/info:"+countryInfoDTO.getCountry()
                       +" given to thread  "+Thread.currentThread().getName()
                       +" id: "+Thread.currentThread().getId());


            return ResponseEntity.ok().body(
                    CountryDenominationInfo.toModel(
                            parseService.getInfoAboutCountry(
                                    countryInfoDTO.getCountry(),lang
                            )
                    )
            );

        }
        catch (LanguageNotExistException | SiteConnectionError  e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){

            return ResponseEntity.status(500).body(ERROR);

        }







    }

    @PutMapping()

    public ResponseEntity<Object> findRequiredCoin(@RequestBody SearchInformation searchInformation, @RequestParam String lang) throws  IOException, ClassNotFoundException, CountryNotExistException {


        try {

            IncomingLangValidator.checkExistLanguage(lang);
            Thread.currentThread().setName(lang);
            IncomingSearcherValidator val=new IncomingSearcherValidator();

            return ResponseEntity.ok().body(
                    parseService.getRequiredCoins(
                            searchInformation.getCountry()
                            ,searchInformation.getYear()
                            ,val.validate(searchInformation, lang)
                            ,lang
                    )
            );
        }
        catch (LanguageNotExistException | CountryNotExistException | SiteConnectionError e){

            return  ResponseEntity.badRequest().body(e.getMessage());

        } catch (ServerWorkException e) {
            return  ResponseEntity.status(500).body(e.getMessage());
        }
        catch (Exception e ){

           return  ResponseEntity.status(500).body(ERROR);

        }



    }


    @GetMapping("/price")
    public ResponseEntity<String> getCoinPrice(@RequestParam  String url, @RequestParam String lang,@Param("vid") String vid){

        try {
            IncomingLangValidator.checkExistLanguage(lang);
            Thread.currentThread().setName(lang);

            return ResponseEntity.ok().body(parseService.getActualCoinCost(url,vid));


        } catch (LanguageNotExistException | SiteConnectionError e) {

            return ResponseEntity.status(500).body(e.getMessage());

        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(ERROR);
        }


    }


}
