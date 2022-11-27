package com.numismatic_app.server.business_components.ucoin_parser;

import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.exception.ServerWorkException;
import com.numismatic_app.server.file_worker.GetterInfo;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.dto.CoinDto;
import com.numismatic_app.server.exception.SiteConnectionError;
import com.numismatic_app.server.file_worker.SaverInfo;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.util.*;


@Data
@Log4j2
public class CoinSearcher {

    public static final String PATH_TO_RESOURCES="./src/main/resources";

    public static final String PATH_TO_UCOIN_PROPERTY = PATH_TO_RESOURCES+"/ucoin.properties";



    private static final String PROP_MAIN_PAGE = "mainPage.";

    private static final String CLASS = "class";
    private static final String WRAP_NOPAD ="wrap nopad";
    private static final String COUNTRY ="country";

     private ArrayList<Set<String>> countries; //список со всеми странами

     public ArrayList<Set<String>> getCountries(){
        return countries;
    }


    public static ArrayList<String> getCountriesFromUcoin(String lang) throws IOException, SiteConnectionError { //отвечает за получение списка стран с сайта ucoin

        try {

            PropertyConnection property = new PropertyConnection(PATH_TO_UCOIN_PROPERTY);


            Document mainPageDoc = UcoinConnection.getUcoinPage(property.open().getProperty("link." + lang));


            SaverInfo saverInfo = new SaverInfo( property.open().getProperty(PROP_MAIN_PAGE + lang));
            saverInfo.save(String.valueOf(mainPageDoc));
            saverInfo.close();
            property.close();

            Elements timeVar = mainPageDoc.getElementsByAttributeValue(CLASS, WRAP_NOPAD);

            //получаем список стран в пользовательском виде

            return new ArrayList<>(new HashSet<>(timeVar.eachText()));

        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }

    }

    public static String getCountryLink(String requiredCountry, String lang) throws IOException, ClassNotFoundException {  //возвращает нужную часть http ссылки на определенную страну
        // название стран для пользователей и их название в http ссылке отличается

        PropertyConnection property = new PropertyConnection(PATH_TO_UCOIN_PROPERTY);

        GetterInfo getParseInfo = new GetterInfo(
                property.open().
                        getProperty(PROP_MAIN_PAGE +lang)
        );
        Document mainPage =Jsoup.parse(String.valueOf(getParseInfo.get()));

        Elements  listOfCountries =  mainPage.getElementsByAttributeValue(CLASS,COUNTRY);
        getParseInfo.close();
        property.close();


        for(Element oneComparedCountry: listOfCountries){

            if(requiredCountry.equals(
                    oneComparedCountry
                            .getElementsByAttributeValue(CLASS,WRAP_NOPAD)
                            .text())
            ) {
                return  oneComparedCountry.select("a[href]").attr("href");
            }


        }

        return "Not found, something wrong";

    }


    public static ArrayList<CoinDto> getCoin(String country, ArrayList<Integer> year, ArrayList<String> curAndValue, String lang) throws IOException, ClassNotFoundException, SiteConnectionError, ServerWorkException {

        FindCoinByYears findCoinByYears =  new FindCoinByYears(getInfoAboutCountry(country,lang),year,curAndValue,lang);


        return new ArrayList<>(findCoinByYears.getCoins());


    }

    public static ArrayList<CoinDto> getCoin(String country,ArrayList<Integer> year, ArrayList<String> period, ArrayList<String> curAndValue, String lang) throws IOException, ClassNotFoundException, SiteConnectionError, ServerWorkException {

        FindCoinByPeriods findCoinByPeriods =  new FindCoinByPeriods(getInfoAboutCountry(country,lang),period,curAndValue,lang);


        return new ArrayList<>(findCoinByPeriods.getCoins());


    }


    public static CountryInformation getInfoAboutCountry(String country, String lang) throws IOException, ClassNotFoundException, SiteConnectionError {

        String partOfLinkCountry = getCountryLink(country, lang); // получает часть ссылки на страну
        String correctCountryName = partOfLinkCountry.substring(18); // извлекает из ссылки название страны для http запрсов и более удобного сравнивания в html коде

        CountryInformation infoAboutCountry;
        PropertyConnection property = new PropertyConnection(PATH_TO_UCOIN_PROPERTY);


        File file = new File(property.open().getProperty("countriesInfo")
                +country+"_"+lang+".txt"
        );



        if(file.length()!=0){

            log.info("exist data about "+country);
            GetterInfo getParseInfo = new GetterInfo(file.getPath());
            infoAboutCountry= (CountryInformation)getParseInfo.get();
            getParseInfo.close();

            return  infoAboutCountry;
        }

        log.info("info about "+country+" empty");

        GetterInfo getParseInfo = new GetterInfo(property.open().getProperty(PROP_MAIN_PAGE +lang));

        Document mainPage =Jsoup.parse( String.valueOf(getParseInfo.get()));
        log.info("successful get main page");

        getParseInfo.close();

        Element htmlCountryPeriods = Optional.ofNullable(mainPage.selectFirst("[data-code="+correctCountryName+"]")).orElse(new Element("null")); // получает html код, внутри которого информация о периодах в запрашиваемой стране с ссылками
        Elements countryPeriods = htmlCountryPeriods.getElementsByAttributeValue(CLASS,"period");

        SaverInfo saverParseInfo = new SaverInfo(file.getAbsolutePath());

        if(countryPeriods!=null){

            try {
                infoAboutCountry=new CountryInformation(countryPeriods, country,lang);
            } catch (SiteConnectionError e) {
                log.error(e.getMessage());
                throw new SiteConnectionError(e.getMessage());
            }
            saverParseInfo.save(infoAboutCountry);
            saverParseInfo.close();
            return infoAboutCountry;
        }

        return null;

    }



    public static String getCoinCostFromUcoin(String url) throws SiteConnectionError {

        try {
            Document coinPage=  UcoinConnection.getUcoinPage(url);

            return Optional.ofNullable(coinPage
                            .getElementsByAttributeValue("href","#price")
                            .first()
                    ).orElse(new Element("a")
                            .prepend("Value:&nbsp;<span>none</span>")
                    )
                    .text()
                    .split(": ",2)[1];


        } catch (SiteConnectionError | IOException e) {
            throw new SiteConnectionError(e.getMessage());
        }

    }


    public static String replaceAmpersand(String text){

        return text.replace("&amp;","&");

    }

}
