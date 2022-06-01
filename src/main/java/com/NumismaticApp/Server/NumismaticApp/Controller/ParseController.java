package com.NumismaticApp.Server.NumismaticApp.Controller;



import com.NumismaticApp.Server.NumismaticApp.DTO.CountryInfoDTO;
import com.NumismaticApp.Server.NumismaticApp.DTO.SearchInformation;
import com.NumismaticApp.Server.NumismaticApp.Exception.AllPeriodsWasParsing;
import com.NumismaticApp.Server.NumismaticApp.Exception.CountryNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Model.CountryDenominationInfo;
import com.NumismaticApp.Server.NumismaticApp.Service.ParseService;
import com.NumismaticApp.Server.NumismaticApp.Validator.IncomingCountryValidator;
import com.NumismaticApp.Server.NumismaticApp.Validator.IncomingSearcherValidator;
import com.NumismaticApp.Server.NumismaticApp.Validator.IncomingLangValidator;
import com.NumismaticApp.Server.NumismaticApp.Model.CountryInfoModel;
import com.fasterxml.jackson.core.io.UTF8Writer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;


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

                IncomingCountryValidator.
                        checkExistCountry(countryInfoDTO.getCountry());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // catch (AllPeriodsWasParsing e) {
         //   return ResponseEntity.badRequest().body(e.getMessage());

       // }





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

        }


    }



}
