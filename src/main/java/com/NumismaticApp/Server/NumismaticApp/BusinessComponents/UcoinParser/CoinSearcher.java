package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.DTO.CoinDto;
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

    public static String pathToUcoinProperty = new File("").getAbsolutePath()+"/src/main/resources/ucoin.properties";

   // private Document mainPageDoc; //html код главной страницы сайта en.ucoin.net


     private ArrayList<Set<String>> countries; //список со всеми странами

   // private HashMap<String,CountryInformation> infoAboutCountries; // содержит в себе название страны и объект CountryInformation, в котором хранится информация о стране
    //черная заготовка кэша

    public CoinSearcher(String lang) throws IOException {  //отвечает за подгрузку нужной инфы для отпимизации поиска




                                                       //подгружает страны в список countries в  пользовательском виде


    }

     public ArrayList<Set<String>> getCountries(){
        return countries;
    }


    public static ArrayList<Set<String>> getCountriesFromUcoin(String lang) throws IOException { //отвечает за получение списка стран с сайта ucoin


        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document mainPageDoc =Jsoup.connect(
                property.open().getProperty("link."+lang)
        ).get();

        SaverParseInfo saverInfo = new SaverParseInfo(new File("").getAbsolutePath()+property.open().getProperty("mainPage."+lang));
        saverInfo.save(String.valueOf(mainPageDoc));
        saverInfo.close();
        property.close();


        log.info("Successful connect to Ucoin");
        Elements timeVar = mainPageDoc.getElementsByAttributeValue("class","wrap nopad");

          //получаем список стран в пользовательском виде

          return  new ArrayList<Set<String>>(new HashSet(timeVar.eachText()));


    }


    public  void smartCountrySelection(String country){

        System.out.println("im working");

    }



    public static String getCountryLink(String requiredCountry) throws IOException, ClassNotFoundException {  //возвращает нужную часть http ссылки на определенную страну
        // название стран для пользователей и их название в http ссылке отличается

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        GetParseInfo getParseInfo = new GetParseInfo(new File("").getAbsolutePath()+
                property.open().
                        getProperty("mainPage."+Thread.currentThread().getName())
        );
        Document mainPage =Jsoup.parse(String.valueOf(getParseInfo.get()));

        Elements  listOfCountries =  mainPage.getElementsByAttributeValue("class","country");
        getParseInfo.close();
        property.close();


        for(Element oneComparedCountry: listOfCountries){

            if(requiredCountry.equals(
                    oneComparedCountry
                            .getElementsByAttributeValue("class","wrap nopad")
                            .text())
            ) {
                return  oneComparedCountry.select("a[href]").attr("href");
            }


        }

        return "Not found, something wrong";

    }


    public static ArrayList<CoinDto> getCoin(String country, ArrayList<Integer> year, ArrayList<String> curAndValue) throws IOException, ClassNotFoundException {

       FindCoin findCoin = new FindCoin(getInfoAboutCountry(country),year,curAndValue);

       System.out.println("From CoinSearch.getCoin:"+findCoin.getLiteCoins());

       return new ArrayList<CoinDto>( findCoin.getCoins());



    }




    public static CountryInformation getInfoAboutCountry(String country) throws IOException, ClassNotFoundException {

        String partOfLinkCountry = getCountryLink(country); // получает часть ссылки на страну
        String correctCountryName = partOfLinkCountry.substring(18); // извлекает из ссылки название страны для http запрсов и более удобного сравнивания в html коде

        CountryInformation infoAboutCountry;
        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        File file = new File(new File("").getAbsolutePath()+property.open().getProperty("countriesInfo")+country+"_"+Thread.currentThread().getName()+".txt");
        System.out.println(file.isFile());

        if(file.length()!=0){

            log.info("exist data about "+country);
            GetParseInfo getParseInfo = new GetParseInfo(file.getPath());
            infoAboutCountry= (CountryInformation)getParseInfo.get();
            getParseInfo.close();

            return  infoAboutCountry;
        }

        log.info("info about "+country+" empty");

        GetParseInfo getParseInfo = new GetParseInfo( new File("").getAbsolutePath()+
                property.open().
                        getProperty("mainPage."+Thread.currentThread().getName())
        );
        Document mainPage =Jsoup.parse( String.valueOf(getParseInfo.get()));

        Element htmlCountryPeriods = mainPage.selectFirst("[data-code="+correctCountryName+"]"); // получает html код, внутри которого информация о периодах в запрашиваемой стране с ссылками
        Elements countryPeriods = htmlCountryPeriods.getElementsByAttributeValue("class","period");

        SaverParseInfo saverParseInfo = new SaverParseInfo(file.getPath());
        if(countryPeriods!=null){

            infoAboutCountry=new CountryInformation(countryPeriods, country);
            saverParseInfo.save(infoAboutCountry);
            saverParseInfo.close();
            return infoAboutCountry;
        }

        return null;

    }




    public static String replaceAmpersand(String text){

        return text.replace("&amp;","&");

    }

}
