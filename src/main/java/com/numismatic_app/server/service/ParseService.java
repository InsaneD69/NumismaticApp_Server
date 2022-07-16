package com.numismatic_app.server.service;

import com.numismatic_app.server.exception.ServerWorkException;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.file_worker.GetterInfo;
import com.numismatic_app.server.dto.CoinDto;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Реализует общение с бизнес-компонентами
 */
@Service
@Log4j2
public class ParseService {


    /**
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @return ArrayList<String> - список стран
     * @throws IOException Выбрасывается при ошибке IO
     * @throws ClassNotFoundException Выбрасывается при ошибке обнаружения
     * классов PropertyConnection и GetterInfo
     */
    public ArrayList<String> getCountryList(String lang) throws IOException, ClassNotFoundException {

        log.info("taken list of country: given to "+Thread.currentThread().getName()+" id: "+Thread.currentThread().getId());

        PropertyConnection property=new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);
        GetterInfo getParseInfo = new GetterInfo(new File("").getAbsolutePath()+property.open().getProperty("countriesList."+lang));

        property.close();

        ArrayList<String> countryList =(ArrayList<String>) getParseInfo.get();

       getParseInfo.close();

        log.info("sending countryList "+lang+" starting with\n"+countryList.get(0));
        return countryList;

    }

    /** Обращается к бизнес-части сервера за информацией об определенной стране
     * @param country Наименование страны, информация о которой запрашивается
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @return Объект класса {@link CountryInformation} в котором содержится информация
     * о запращиваемой стране
     * @throws SiteConnectionError выбрасывается при ошибке подключения к сайту ucoin
     * (возможно он заблокировал IP сервера и необходимо пройти капчу на сайте,
     * совершив вход на сайт через браузер)
     * @throws ServerWorkException выбрасывается при ошибке на  сервере,
     * которая  связанна с файловой системой сервера
     */
    public CountryInformation getInfoAboutCountry(String country,String lang) throws SiteConnectionError, ServerWorkException {

        try {

            return  CoinSearcher.getInfoAboutCountry(country,lang);

        } catch (SiteConnectionError  e) {

            log.error(e.getMessage());
            throw new SiteConnectionError(e.getMessage());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServerWorkException(e.getMessage());

        }

    }


    /** Обращается к бизнес-части сервера за получением списка монет,
     * подходящих под требования запроса
     * @param country  Наименование страны, в которой требуется найти монеты
     * @param year  Год запращиваемой монеты
     * @param curAndVal Массив из склеенных каждый друг с другом  номиналов и валют
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @return Список из монет {@link CoinDto}, которые подошли под требования
     */
    public Object getRequiredCoins(String country, ArrayList<Integer> year,ArrayList<String> period, ArrayList<String> curAndVal ,String lang) throws IOException, ClassNotFoundException, SiteConnectionError, ServerWorkException {

        log.info("curAndVal: "+curAndVal);

        ArrayList<CoinDto> coinDtos = new ArrayList<>();

        if(year.isEmpty()){

            coinDtos.addAll( CoinSearcher.getCoin(country,null,period,curAndVal,lang));

        }
        else if(year.get(0)==99999){

            coinDtos.addAll( CoinSearcher.getCoin(country,null,period,curAndVal,lang));

        }
        else{
            coinDtos.addAll(CoinSearcher.getCoin(country,year,curAndVal,lang));
        }


        if(coinDtos.isEmpty()){

            return "coins with the specified parameters were not found";

        }

        else  return  coinDtos;

    }


    /** Обращается к бизнес-части сервера за получением цены определенной монеты,
     *     формирует правильный url для запроса
     * @param partOfCoinUrl Часть сслыки на монету (например:/coin/russia-10-kopeks-2008/?cid=1981)
     * @param vid Также часть ссылки на моненту, но может отсутствовать
     * @param lang Указывает аббревиатуру языка, на котором требуется ответ
     * @return Возвращает цену монеты на запрашиваемом языке с валютой в String формате
     */
    public String getActualCoinCost(String partOfCoinUrl, String vid,String lang) throws IOException, SiteConnectionError {

        PropertyConnection prop = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        String coinUrl = prop.open().getProperty("link."+lang)+"/"+partOfCoinUrl+"&vid="+ Optional.ofNullable(vid).orElse("");

        prop.close();

        return   CoinSearcher.getCoinCostFromUcoin(coinUrl);

    }



}
