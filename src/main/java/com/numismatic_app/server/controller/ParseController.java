package com.numismatic_app.server.controller;

import com.numismatic_app.server.dto.CountryInfoDTO;
import com.numismatic_app.server.dto.SearchInformation;
import com.numismatic_app.server.dto.CountryDenominationInfo;
import com.numismatic_app.server.exception.CountryNotExistException;
import com.numismatic_app.server.exception.LanguageNotExistException;
import com.numismatic_app.server.exception.ServerWorkException;
import com.numismatic_app.server.service.ParseService;
import com.numismatic_app.server.validator.IncomingCountryValidator;
import com.numismatic_app.server.validator.IncomingSearcherValidator;
import com.numismatic_app.server.validator.IncomingLangValidator;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;


/**
 * Обрабатывает запросы клиента, связанные с поиском монет
 */
@RestController
@RequestMapping("/search")
@Log4j2
public class ParseController {

    private static final String ERROR = "Ошибка сервера";

    @Autowired
    private ParseService parseService;


    /**
     * @param lang Указывает аббревиатуру языка, на котором требуется отве
     * @return Возвращает список стран
     */
    @GetMapping("/countries")
    public ResponseEntity<Object> getCountries(@RequestParam String lang)  {

        try {
            IncomingLangValidator.checkExistLanguage(lang);


            log.info("taken Get request /search/countries: given to thread  "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());
            return ResponseEntity.ok().body(parseService.getCountryList(lang));
        }
        catch (LanguageNotExistException e){

           return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IOException |ClassNotFoundException e) {

            return ResponseEntity.status(500).body(ERROR);

        }



    }

    /** Обрабатывает запросы клиента по пути /search/info, получает номиналы и
     * валюты определенной страны
     *
     * @param countryInfoDTO Содержит в себе поля, необходимые для получения списка
     *   валют и номиналов{@link CountryInfoDTO}
     * @param lang  Указывает аббревиатуру языка, на котором требуется ответ
     * @return Возвращает объект класса {@link CountryDenominationInfo} или ошибку
     */
    @PutMapping("/info")
    public ResponseEntity<Object> parseIncomingInfo(@RequestBody CountryInfoDTO countryInfoDTO, @RequestParam String lang)  {

        try{
                IncomingLangValidator
                        .checkExistLanguage(lang);

                IncomingCountryValidator
                        .checkExistCountry(countryInfoDTO.getCountry(),lang);

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
            e.printStackTrace();
            return ResponseEntity.status(500).body(ERROR);

        }







    }

    /** Обрабатывает запросы клиента по пути /search/ и получет монеты по заданным критериям
     * @param searchInformation Содержит поля класса {@link SearchInformation},
     *                          которые необходимы для поиска монет
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @return Возвращает список монет (объекты класса {@link com.numismatic_app.server.dto.CoinDto})
     */
    @PutMapping()
    public ResponseEntity<Object> findRequiredCoins(@RequestBody SearchInformation searchInformation, @RequestParam String lang)  {


        try {

            IncomingLangValidator.checkExistLanguage(lang);
            IncomingCountryValidator
                    .checkExistCountry(searchInformation.getCountry(),lang);

            return ResponseEntity.ok().body(
                    parseService.getRequiredCoins(
                             searchInformation.getCountry()
                            ,searchInformation.getYear()
                            ,IncomingSearcherValidator.validateCurrenciesAndValues(searchInformation)
                            ,lang
                    )
            );
        }
        catch (LanguageNotExistException | CountryNotExistException | SiteConnectionError e){
            log.info(e.getMessage());
            return  ResponseEntity.badRequest().body(e.getMessage());

        } catch (ServerWorkException e) {
            log.info(e.getMessage());
            return  ResponseEntity.status(500).body(e.getMessage());

        } catch (IOException |  ClassNotFoundException e) {

            e.printStackTrace();

            return  ResponseEntity.status(500).body("Server error");
        }


    }


    /** Обрабатывает запросы клиента по пути /search/price и получает цену на монету
     * @param url Часть сслыки на монету (например:/coin/russia-10-kopeks-2008/?cid=1981)
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @param vid Также часть ссылки на моненту, но может отсутствовать
     * @return Возвращает цену запрашиваемой монеты в String формате
     */
    @GetMapping("/price")
    public ResponseEntity<String> getCoinPrice(@RequestParam  String url, @RequestParam String lang,@Param("vid") String vid){

        try {

            IncomingLangValidator.checkExistLanguage(lang);

            return ResponseEntity.ok().body(parseService.getActualCoinCost(url,vid,lang));

        } catch (LanguageNotExistException | SiteConnectionError e) {

            return ResponseEntity.status(500).body(e.getMessage());

        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(ERROR);
        }

    }

}
