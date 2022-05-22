package com.NumismaticApp.Server.NumismaticApp.Controller;


import com.NumismaticApp.Server.NumismaticApp.DTO.SearchInformation;
import com.NumismaticApp.Server.NumismaticApp.Exception.CountryNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Service.ParseService;
import com.NumismaticApp.Server.NumismaticApp.Validator.IncomingCountryValidator;
import com.NumismaticApp.Server.NumismaticApp.Validator.IncomingValidator;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/search")
@Log4j2
public class ParseController {

    @Autowired
    private ParseService parseService;


    @GetMapping("/countries")

    private ResponseEntity getCountries(@RequestParam String lang) throws IOException, ClassNotFoundException {

        try {
            IncomingValidator.checkExistLanguage(lang);

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

    @GetMapping("/info")
    private ResponseEntity parseIncomingInfo(@RequestBody SearchInformation searchInformation,
                                             @RequestParam String lang) throws IOException, ClassNotFoundException, InterruptedException {

        try{

            IncomingValidator.checkExistLanguage(lang);
            Thread.currentThread().setName(lang);
            IncomingCountryValidator.checkExistCountry(searchInformation.getCountry());
            log.info("taken Get request /search/info:"+searchInformation.getCountry()+" given to thread  "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());


            return ResponseEntity.ok().body(parseService.getInfoAboutCountry(searchInformation.getCountry()));

        }
        catch (LanguageNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (CountryNotExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }



    }




}
