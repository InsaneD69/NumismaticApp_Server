package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.DTO.CoinDto;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;

@Log4j2
public class FindCoin {


    private  ArrayList<Integer> year;
    private  ArrayList<String> corAndValue;
    private CountryInformation countryInformation;
    private Set<CountryPeriod> countryPeriods;
    private Set<LiteCoin> liteCoins;

    private Set<CoinDto> coins;


    public FindCoin(CountryInformation countryInfo, ArrayList<Integer> year, ArrayList<String> corAndValue){

        this.year=year;
        this.corAndValue=corAndValue;
        this.countryInformation=countryInfo;

        System.out.println("countryInfo:"+countryInfo.periods.get(0).getNamePeriod());
        System.out.println("years: "+year);
        System.out.println("corAndValue: "+corAndValue);


        findSuitablePeriods();

        if(countryPeriods!=null){

            findSuitableLiteCoins();

                if( liteCoins!=null) {
                    log.info(liteCoins.toString());
                    findSuitableCoins();

                   }
        }

    }


    private void findSuitablePeriods(){

        countryPeriods=new HashSet<>();

        countryInformation.periods.forEach((period)->{

            year.forEach(year->{

                if(period.compareData(year)){

                    countryPeriods.add(period);
                    log.info("period "+period.getNamePeriod()+" was added");

                }

            });


        });

    }

    private void findSuitableLiteCoins(){

        liteCoins = new HashSet<>();


        countryPeriods.forEach(countryPeriod -> {


           countryPeriod.getListOnePeriodCountry().forEach(liteCoin -> {

               corAndValue.forEach(elem->{

                 if(   (liteCoin.getValueAndCurrency().split(" ")[0].equals(elem.replace(" ",""))
                         ||
                         liteCoin.getValueAndCurrency().split(" ")[1].equals(elem.replace(" ",""))
                         ||
                         liteCoin.getValueAndCurrency().equals(elem)
                       )
                         &&
                         year.contains(liteCoin.getYear())

                 ){

                     liteCoins.add(liteCoin);
                 }

               });



           });


        });


    }

    private void findSuitableCoins(){

        liteCoins.forEach(liteCoin->{

            try {

              getCoins(liteCoin.getUrl(),liteCoin.getYear());

            } catch (IOException e) {
                e.printStackTrace();
            }


        });




    }

    public  CoinDto getCoins(String url, Integer year ) throws IOException {

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+Thread.currentThread().getName())+url).get();
        property.close();
        Element table=doc.select("table").get(3);  //таблица

        Elements  tableCoins = table.getElementsByAttributeValue("class","tr-hr")
                .stream()
                .filter(coin->
                         Integer.compare(Integer.parseInt(coin.getElementsByTag("strong").text()),year)==0
                 )
                .collect(Collectors.toCollection(Elements::new));


        tableCoins.forEach(coin->{

            try {
                getCoin(coin.attr("data-href"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        });


        System.out.println(tableCoins);
        return null;


    }


    public  CoinDto getCoin(String url ) throws IOException {

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+Thread.currentThread().getName())+url).get();
        property.close();
        //System.out.println("----------------------------");
        // Element ment=doc.select("table").get(0);
        //System.out.println(0+"----------------------------");
      //  System.out.println(ment);

        Element ement=doc.select("table").get(1);
        System.out.println("----------------------------");
        System.out.println(ement);
        System.out.println("----------------------------");
       // Element lement=doc.select("table").get(2);
       // System.out.println(2+"----------------------------");
       // System.out.println(lement);
       // Element table=doc.select("table").get(3);
       // System.out.println(3+"----------------------------");
        //System.out.println(table);





    return null;
    }

    public ArrayList<Integer> getYear() {
        return year;
    }

    public void setYear(ArrayList<Integer> year) {
        this.year = year;
    }

    public ArrayList<String> getCorAndValue() {
        return corAndValue;
    }

    public void setCorAndValue(ArrayList<String> corAndValue) {
        this.corAndValue = corAndValue;
    }

    public CountryInformation getCountryInformation() {
        return countryInformation;
    }

    public void setCountryInformation(CountryInformation countryInformation) {
        this.countryInformation = countryInformation;
    }

    public Set<CountryPeriod> getCountryPeriods() {
        return countryPeriods;
    }

    public void setCountryPeriods(Set<CountryPeriod> countryPeriods) {
        this.countryPeriods = countryPeriods;
    }

    public Set<LiteCoin> getLiteCoins() {
        return liteCoins;
    }

    public void setLiteCoins(Set<LiteCoin> liteCoins) {
        this.liteCoins = liteCoins;
    }

    public Set<CoinDto> getCoins() {
        return coins;
    }

    public void setCoins(Set<CoinDto> coins) {
        this.coins = coins;
    }
}
