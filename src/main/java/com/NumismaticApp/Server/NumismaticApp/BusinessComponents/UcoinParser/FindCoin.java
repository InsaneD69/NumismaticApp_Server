package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.DTO.CoinDto;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;

@Log4j2
public class FindCoin {


    private  ArrayList<Integer> years;
    private  ArrayList<String> corAndValue;
    private CountryInformation countryInformation;
    private Set<CountryPeriod> countryPeriods;
    private Set<LiteCoin> liteCoins;

    private Set<CoinDto> coins;


    public FindCoin(CountryInformation countryInfo, ArrayList<Integer> year, ArrayList<String> corAndValue) throws IOException {

        this.years =year;
        this.corAndValue=corAndValue;
        this.countryInformation=countryInfo;
        coins=new HashSet<>();

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


    private void findSuitablePeriods() throws IOException {

        countryPeriods=new HashSet<>();

       int countryStatus = countryInformation.hashCode();

        countryInformation.periods.forEach((period)->{

            years.forEach(year->{

                if(period.compareData(year)){

                    if (period.getListOnePeriodCountry()==null){

                        try {
                            period.setCurrenciesAndNominalValues();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    countryPeriods.add(period);


                }

            });


        });

        if(countryStatus!=countryInformation.hashCode()){

            PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

            SaverParseInfo saverParseInfo = new SaverParseInfo(
                    new File("").getAbsolutePath()+
                            property.open().getProperty("countriesInfo")
                            +countryInformation.getNameCountry()
                            +"_"+Thread.currentThread().getName()+".txt"
            );

            saverParseInfo.save(countryInformation);
            saverParseInfo.close();
            log.info("update info about "+countryInformation.getNameCountry());


        }

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
                         years.contains(liteCoin.getYear())

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

              getCoins(liteCoin.getUrl(),liteCoin.getYear(),liteCoin.getValueAndCurrency());

            } catch (IOException e) {
                e.printStackTrace();
            }


        });




    }

    public  CoinDto getCoins(String url, Integer year ,String valueAndCurrency) throws IOException {

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+Thread.currentThread().getName())+url).get();
        property.close();

        Element  tableCoins = doc.getElementsByAttributeValue("class","tbl").tagName("body").first(); // html таблица с монетами


        Elements coinHtml = tableCoins
                .getElementsByAttributeValue("class","tr-hr")
                .stream()
               .filter(coin->
                     Integer.compare(
                             Integer.parseInt(coin.getElementsByTag("strong").text())
                             ,year
                     )==0
               )
            .collect(Collectors.toCollection(Elements::new));



       coinHtml.forEach(coin->{

            try {
                String value=valueAndCurrency.split(" ",2)[0];
                String currency=valueAndCurrency.split(" ",2)[1];
                getCoin(coin.attr("data-href"),value,currency,year);
           } catch (IOException e) {
               throw new RuntimeException(e);
            }


        });



        return null;


    }


    public  void getCoin(String url, String value, String currency, Integer year) throws IOException {


        CoinDto coinDto = new CoinDto();

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+Thread.currentThread().getName())+url).get();
        property.close();

        Elements infoTableColumns = doc
                .select("table")
                .get(1)
                .getElementsByTag("tr");


        infoTableColumns.remove(0); //удаляем ненужный krause number
        coinDto.setInfoTable();

        infoTableColumns.forEach(r->{
                        coinDto.addToInfoTable(
                                r.getElementsByTag("th").text(),
                                r.getElementsByTag("td").text()
                        );
        });

        coinDto.setCurrency(currency);
        coinDto.setValue(value);
        coinDto.setYears(year);


       coinDto.setCost(
               doc.getElementsByAttributeValue("href","#price").first()
                       .text()
                       .split(": ")[1]
       );


        coinDto.setMint(

                        Optional.ofNullable(Optional.ofNullable(doc.getElementsByAttributeValue("class","sodd")
                                                .first())
                                        .orElse(
                                                new Element("<th></th>")
                                        ).getElementsByTag("td").first()).orElse(new Element("<th></th>"))
                                .text()

        );

        coinDto.setCountry(countryInformation.getNameCountry());
        coinDto.setDataOfCreate(LocalDate.now());
        coinDto.setLinkUcoin(url);
        coins.add(coinDto);

    }




    public ArrayList<Integer> getYears() {
        return years;
    }

    public void setYears(ArrayList<Integer> years) {
        this.years = years;
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
