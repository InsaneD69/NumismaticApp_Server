package com.NumismaticApp.Server.NumismaticApp.Controller;


import com.NumismaticApp.Server.NumismaticApp.Exception.LanguageNotExistException;
import com.NumismaticApp.Server.NumismaticApp.Service.ParseService;
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

        log.info("taken Get request /search/countries");

        try {
            parseService.checkExistLanguage(lang);
            return ResponseEntity.ok().body(parseService.getCountryList(lang));
        }
        catch (LanguageNotExistException e){

           return ResponseEntity.badRequest().body(e.getMessage());

        }



    }



}
