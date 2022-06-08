package com.numismatic_app.server.business_components.ucoin_parser;

import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryPeriod;
import com.numismatic_app.server.business_components.ucoin_parser.objects.LiteCoin;
import com.numismatic_app.server.exception.ServerWorkException;
import com.numismatic_app.server.controller.file_worker.PropertyConnection;
import com.numismatic_app.server.dto.CoinDto;
import com.numismatic_app.server.exception.SiteConnectionError;
import com.numismatic_app.server.controller.file_worker.SaverInfo;
import lombok.extern.log4j.Log4j2;
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




@Log4j2
public class FindCoin {

    private static final String CLASS = "class";


    private  ArrayList<Integer> years;
    private  ArrayList<String> corAndValue;
    private CountryInformation countryInformation;
    private Set<CountryPeriod> countryPeriods;
    private Set<LiteCoin> liteCoins;

    private Set<CoinDto> coins;

    private String lang;

    public FindCoin(CountryInformation countryInfo, ArrayList<Integer> year, ArrayList<String> corAndValue, String lang) throws IOException, ServerWorkException, SiteConnectionError {

        this.years =year;
        this.corAndValue=corAndValue;
        this.countryInformation=countryInfo;
        this.lang=lang;
        coins=new HashSet<>();

             findSuitablePeriods();

             if(!countryPeriods.isEmpty()){

                  findSuitableLiteCoins();

                      if( liteCoins!=null) {
                          log.info(liteCoins.toString());
                          findSuitableCoins();

                         }
             }



    }


    private void findSuitablePeriods() throws IOException, SiteConnectionError {

        countryPeriods=new HashSet<>();

        ArrayList<CountryPeriod> addingPeriods = new ArrayList<>();

       int countryStatus = countryInformation.hashCode();

        countryInformation.getPeriods().forEach(period->

            years.forEach(year->{

                if(period.compareData(year)){

                    log.debug("added for searching " +period.getNamePeriod() +" period");
                    addingPeriods.add(period);

                }
            })
        );



        for (CountryPeriod period: addingPeriods){

            try {

                if(period.getListOnePeriodCountry()==null){

                     period.setCurrenciesAndNominalValues(lang);
                }
                if(period.getCurrenciesAndNominalValues()==null){

                    continue;
                }
                countryPeriods.add(period);

            } catch (SiteConnectionError e) {

                throw new SiteConnectionError(e.getMessage());
            }

        }

        if(countryStatus!=countryInformation.hashCode()){

            PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

            SaverInfo saverParseInfo = new SaverInfo(
                    new File("").getAbsolutePath()+
                            property.open().getProperty("countriesInfo")
                            +countryInformation.getNameCountry()
                            +"_"+lang+".txt"
            );

            saverParseInfo.save(countryInformation);
            saverParseInfo.close();
            log.info("update info about "+countryInformation.getNameCountry());


        }

    }

    private void findSuitableLiteCoins(){

        liteCoins = new HashSet<>();


        countryPeriods.forEach(countryPeriod ->


           countryPeriod.getListOnePeriodCountry().forEach(liteCoin ->

               corAndValue.forEach(elem->{

                 if(   (liteCoin.getValueAndCurrency().split(" ",2)[0].replace(" ","").equals(elem.replace(" ",""))
                         ||
                         liteCoin.getValueAndCurrency().split(" ",2)[1].replace(" ","").equals(elem.replace(" ",""))
                         ||
                         liteCoin.getValueAndCurrency().equals(elem)
                       )
                         &&
                         years.contains(liteCoin.getYear())

                 ){
                     liteCoins.add(liteCoin);
                 }

               })

           )

        );


    }

    private void findSuitableCoins() throws SiteConnectionError, ServerWorkException {

        try {
            for(LiteCoin liteCoin:liteCoins){

                  getCoins(liteCoin.getUrl(),liteCoin.getYear(),liteCoin.getValueAndCurrency());

            }
        } catch (SiteConnectionError  e) {
            throw new SiteConnectionError(e.getMessage());
        } catch (Exception e) {
            throw new ServerWorkException("Server error");
        }




    }

    public  void getCoins(String url, Integer year ,String valueAndCurrency) throws SiteConnectionError, IOException, ServerWorkException {

        PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        Document doc ;
        try {
            doc = UcoinConnection.getUcoinPage(property.open().getProperty("link."+lang)+url);

        } catch (SiteConnectionError e) {
           throw new SiteConnectionError(e.getMessage());
        }
        finally {
            property.close();
        }


        Element  tableCoins = doc.getElementsByAttributeValue(CLASS,"tbl").tagName("body").first(); // html таблица с монетами


        log.info(tableCoins);
        Elements coinHtml = tableCoins
                .getElementsByAttributeValue(CLASS,"tr-hr")
                .stream()
               .filter(coin->
                     Integer.compare(
                             Integer.parseInt(coin.getElementsByTag("strong").text())
                             ,year
                     )==0
               )
            .collect(Collectors.toCollection(Elements::new));




        try{
            if(coinHtml.isEmpty()){

                coinHtml = isThereReallyNoTable(tableCoins);

                if (coinHtml.isEmpty()){
                    String value = valueAndCurrency.split(" ", 2)[0];
                    String currency = valueAndCurrency.split(" ", 2)[1];
                    getCoin(url, value, currency, year);
                }
            }
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }



        try {

            for (Element coin: coinHtml) {

                String value = valueAndCurrency.split(" ", 2)[0];
                String currency = valueAndCurrency.split(" ", 2)[1];
                getCoin( Optional.ofNullable( elementFixer(coin.attr("data-href")))
                                .orElse(url.split("/?tid",2)[0]+
                                        coin.getElementsByTag("a").attr("href")
                                                .replace("?",""))
                        , value
                        , currency
                        , year);

            }

        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }
        catch (Exception e){
            throw new ServerWorkException("Server error");

        }

    }


    public  void getCoin(String url, String value, String currency, Integer year) throws IOException, SiteConnectionError {

        CoinDto coinDto = new CoinDto();

        PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        Document doc = null;
        try {
            doc = UcoinConnection.getUcoinPage(property.open().getProperty("link."+lang)+url);
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }finally {
            property.close();
        }


        Element infoTableColumns = doc.getElementsByAttributeValue(CLASS,"tbl coin-info").first();


        Elements infoTables=infoTableColumns.getElementsByTag("tr");


        infoTables.remove(0); //удаляем ненужный krause number
        coinDto.setInfoTable();

        infoTables.forEach(r->
                        coinDto.addToInfoTable(
                                r.getElementsByTag("th").text(),
                                r.getElementsByTag("td").text()
                        )
        );

        coinDto.setCurrency(currency);
        coinDto.setValue(value);
        coinDto.setYears(year);


       coinDto.setCost(Optional.ofNullable(doc
                       .getElementsByAttributeValue("href","#price")
                       .first()
               ).orElse(new Element("a")
                       .prepend("Value:&nbsp;<span>none</span>")
               )
               .text()
               .split(": ",2)[1]

       );


        coinDto.setMint(

                        Optional.ofNullable(Optional.ofNullable(doc.getElementsByAttributeValue(CLASS,"sodd")
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



    private Elements isThereReallyNoTable(Element tableCoins){
        log.info("isThereReallyNoTable?");

        return tableCoins.getElementsByAttribute("href")
                .stream().filter(coin-> !coin
                        .getElementsByTag("a")
                        .attr("href")
                        .contains("#price"))
                .collect(Collectors.toCollection(Elements::new));

    }


    private String elementFixer(String el){
        if(el.equals("")){
            return null;
        }
        else return  el;

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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
