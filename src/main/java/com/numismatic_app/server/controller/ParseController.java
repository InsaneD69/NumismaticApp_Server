package com.numismatic_app.server.controller;



import com.numismatic_app.server.dto.CountryInfoDTO;
import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.dto.CountryDenominationInfo;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.exception.LanguageNotExistException;
import com.numismatic_app.server.service.ParseService;
import com.numismatic_app.server.validator.IncomingCountryValidator;
import com.numismatic_app.server.validator.IncomingDegreeValidator;
import com.numismatic_app.server.validator.IncomingSearcherValidator;
import com.numismatic_app.server.validator.IncomingLangValidator;
import com.numismatic_app.server.exception.DegreeErrorException;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@RestController
@RequestMapping("/search")
@Log4j2
public class ParseController {

    @Autowired
    private ParseService parseService;


    @GetMapping("/countries")
    private ResponseEntity getCountries(@RequestParam String lang) throws IOException, ClassNotFoundException {

        try {
            IncomingLangValidator.checkExistLanguage(lang);

            Thread.currentThread().setName(lang);
            log.info("taken Get request /search/countries: given to thread  "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());
            return ResponseEntity.ok().body(parseService.getCountryList());
        }
        catch (LanguageNotExistException e){

           return ResponseEntity.badRequest().body(e.getMessage());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @PutMapping("/info")
    private ResponseEntity parseIncomingInfo(@RequestBody CountryInfoDTO countryInfoDTO, @RequestParam String lang)  {

        try{
                IncomingLangValidator
                        .checkExistLanguage(lang);

                 Thread.currentThread().setName(lang);
                IncomingCountryValidator
                        .checkExistCountry(countryInfoDTO.getCountry());

                IncomingDegreeValidator
                    .checkDegree(countryInfoDTO.getDegree());



            log.info("taken Get request /search/info:"+countryInfoDTO.getCountry()
                       +" given to thread  "+Thread.currentThread().getName()
                       +" id: "+Thread.currentThread().getId());


            return ResponseEntity.ok().body(
                    CountryDenominationInfo.toModel(
                            parseService.getInfoAboutCountry(
                                    countryInfoDTO.getCountry()
                            )
                    )
            );

        }
        catch (LanguageNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (CountryNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (DegreeErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SiteConnectionError e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        // catch (Exception e) {
          //  return ResponseEntity.status(500).build();

      //  }





    }

    @PutMapping()

    private ResponseEntity findRequiredCoin(@RequestBody SearchInformation searchInformation, @RequestParam String lang) throws  IOException, ClassNotFoundException, CountryNotExistException {


        try {

            IncomingLangValidator.checkExistLanguage(lang);
            Thread.currentThread().setName(lang);
            IncomingSearcherValidator val=new IncomingSearcherValidator();

            return ResponseEntity.ok().body(
                    parseService.getRequiredCoins(
                            searchInformation.getCountry()
                            ,searchInformation.getYear()
                            ,val.validate(searchInformation)
                    )
            );
        }
        catch (LanguageNotExistException e){

            return  ResponseEntity.badRequest().body(e.getMessage());

        }
        catch (CountryNotExistException e){

            return  ResponseEntity.badRequest().body(e.getMessage());

        } catch (SiteConnectionError e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }


    }


    @GetMapping("/price")
    private ResponseEntity getCoinPrice(@RequestParam  String url, @RequestParam String lang){

        try {
            IncomingLangValidator.checkExistLanguage(lang);
            Thread.currentThread().setName(lang);

            return ResponseEntity.ok().body(parseService.getActualCoinCost(url));


        } catch (LanguageNotExistException e) {

            return ResponseEntity.status(500).body(e.getMessage());

        }  catch (SiteConnectionError e) {

           return ResponseEntity.status(500).body(e.getMessage());
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Server error");
        }


    }


}
