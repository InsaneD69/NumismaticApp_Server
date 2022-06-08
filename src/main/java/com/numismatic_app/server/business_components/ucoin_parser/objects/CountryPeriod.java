package com.numismatic_app.server.business_components.ucoin_parser.objects;

import com.numismatic_app.server.business_components.ucoin_parser.UcoinConnection;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher.PATH_TO_UCOIN_PROPERTY;

@Log4j2
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
    private ArrayList<LiteCoin> listOnePeriodCountry;

    private transient Document periodTablePage; //html код странцы с таблицей всех номиналов и годов периода

    public ArrayList<LiteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }

///gdfrzsdfzgdfgdfgd

    public Set<String> getCurrencies() {
        return currencies;
    }

    public Set<String> getNominalValues() {
        return nominalValues;
    }

    public void setCurrenciesAndNominalValues(String lang) throws IOException, SiteConnectionError { //излекает из html таблицы значения номиналов и валют в данном периоде

        //если на странице с таблицей ссылка указаеная не с type=1, то это означает, что таблица на этой странице не с монетами регулярного выпуска


       PropertyConnection property=new PropertyConnection(PATH_TO_UCOIN_PROPERTY);

       try {
           periodTablePage = UcoinConnection.getUcoinPage(property.open()
                   .getProperty("link." + lang)
                   + link);
       } catch (SiteConnectionError e) {

           log.info("CurrenciesAndNominalValues not set for "+namePeriod);
           throw new SiteConnectionError(e.getMessage());

       }
       finally {
           property.close();
       }



        if(!periodTablePage.getElementsByAttributeValue("class","switcher active").attr("href").contains("type=1"))
        { log.info("not exist period, circulation coin is empty"); return;}


       Elements elWithCurAndVal=periodTablePage.getElementsByAttributeValue("class","legend");

       currenciesAndNominalValues =new HashMap<>();

       elWithCurAndVal.forEach(text->{                         //разделяет полученный текст вида 1pf - 1 penning на значение в таблице и номинал с валютой
                                                                 // затем помещает их в мапу
               String[] parts = text.text().split(" - ");
               currenciesAndNominalValues.put(parts[0], parts[1]);

       });

       currencies=new HashSet<>();
       nominalValues = new HashSet<>();



       currenciesAndNominalValues.forEach((tableKey,nomAndCurVal)->{

            String [] parts = nomAndCurVal.split(" ",2);
            nominalValues.add(parts[0]);
            currencies.add(parts[1]);


       });


        InfoAboutCoinsInPeriod infoAboutCoinsInPeriod = new InfoAboutCoinsInPeriod(periodTablePage,currenciesAndNominalValues);

        listOnePeriodCountry=infoAboutCoinsInPeriod.getListOnePeriodCountry();



    }





    public CountryPeriod(Element period,String country) {

        this.country=country;
        this.link= period.attr("href");
        this.namePeriod=period.attr("title");
        String[] intervals = period.getElementsByTag("div").text().split(" - ");
        this.bgYear=Short.parseShort(intervals[0]);
        this.endYear=Short.parseShort(intervals[1]);

    }


    public boolean compareData(int year){ // дает ответ на вопрос: принадлежит ли входящий год к этому периоду


        return ((year>=bgYear)&&(year<=endYear));


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

    @Override
    public String toString() {
        return "CountryPeriod{" +
                "country='" + country + '\'' +
                ", link='" + link + '\'' +
                ", namePeriod='" + namePeriod + '\'' +
                ", bgYear=" + bgYear +
                ", endYear=" + endYear +
                ", currenciesAndNominalValues=" + currenciesAndNominalValues +
                ", currencies=" + currencies +
                ", nominalValues=" + nominalValues +
                ", listOnePeriodCountry=" + listOnePeriodCountry +
                ", periodTablePage=" + periodTablePage +
                '}';
    }

    @Override
    public int hashCode() {
        return namePeriod.hashCode()+Optional.ofNullable(getListOnePeriodCountry()).orElse(new ArrayList<>()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryPeriod that = (CountryPeriod) o;
        return   bgYear == that.bgYear &&
                endYear == that.endYear &&
                Objects.equals(country, that.country) &&
                Objects.equals(link, that.link) &&
                Objects.equals(namePeriod, that.namePeriod);



    }
}
