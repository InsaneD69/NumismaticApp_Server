package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;


public class CountryPeriod implements Serializable { // содержит в себе информацию об одном периоде :

    private  String country;

    public String getCountry() {
        return country;
    }

    public Map<String, String> getCurrenciesAndNominalValues() {
        return currenciesAndNominalValues;
    }

    private String link;             // часть сслыки на таблицу в виде /table/?country=germany&period=1
    private String namePeriod;       // название периода
    private short bgYear;            // начало периода
    private short endYear;           // конец периода
    private Map<String,String> currenciesAndNominalValues; // ключи -  номинал и валюта, значение - обозначение в таблице
    private Set<String> currencies;
    private Set<String> nominalValues;
    private ArrayList<liteCoin> listOnePeriodCountry;

    private transient Document periodTablePage; //html код странцы с таблицей всех номиналов и годов периода

    public ArrayList<liteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }

///gdfrzsdfzgdfgdfgd

    public Set<String> getCurrencies() {
        return currencies;
    }

    public Set<String> getNominalValues() {
        return nominalValues;
    }

    protected void setCurrenciesAndNominalValues() throws IOException { //излекает из html таблицы значения номиналов и валют в данном периоде

        PropertyConnection property=new PropertyConnection(pathToUcoinProperty);

       periodTablePage=Jsoup.connect(
               property.open()
                       .getProperty("link."+Thread.currentThread().getName())
                  +link)
               .get();
        property.close();
       Elements elWithCurAndVal=periodTablePage.getElementsByAttributeValue("class","legend");

       currenciesAndNominalValues =new HashMap<>();

       elWithCurAndVal.forEach((text)->{                         //разделяет полученный текст вида 1pf - 1 penning на значение в таблице и номинал с валютой
                                                                // затем помещает их в мапу
            String[] parts = text.text().split(" - ");
            currenciesAndNominalValues.put(parts[1],parts[0]);

       });

       currencies=new HashSet<>();
       nominalValues = new HashSet<>();

       currenciesAndNominalValues.forEach((nomAndCurKey,tableVal)->{

            String [] parts = nomAndCurKey.split(" ",2);
            nominalValues.add(parts[0]);
            currencies.add(parts[1]);


       });
       System.out.println(currenciesAndNominalValues);


        InformationAboutCoinsInOnePeriod informationAboutCoinsInOnePeriod = new InformationAboutCoinsInOnePeriod(periodTablePage);

        listOnePeriodCountry=informationAboutCoinsInOnePeriod.getListOnePeriodCountry();



    }





    public CountryPeriod(Element period,String country) throws IOException {

        this.country=country;
        this.link= period.attr("href");
        this.namePeriod=period.attr("title");
        String[] intervals = period.getElementsByTag("div").text().split(" - ");
        this.bgYear=Short.parseShort(intervals[0]);
        this.endYear=Short.parseShort(intervals[1]);

    }


    public boolean compareData(int year){ // дает ответ на вопрос: принадлежит ли входящий год к этому периоду

        if((year>=bgYear)&&(year<=endYear)){
            return true;
        } else return false;

    }

    public String getLink() {
        return link;
    }

    public String getNamePeriod() {
        return namePeriod;
    }

    public short getBgYear() {
        return bgYear;
    }

    public short getEndYear() {
        return endYear;
    }
}
