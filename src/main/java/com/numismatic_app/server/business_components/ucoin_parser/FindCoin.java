package com.numismatic_app.server.business_components.ucoin_parser;

import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryPeriod;
import com.numismatic_app.server.business_components.ucoin_parser.objects.LiteCoin;
import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.dto.CoinDto;
import com.numismatic_app.server.exception.SiteConnectionError;
import com.numismatic_app.server.file_worker.SaverInfo;
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

        System.out.println("countryInfo:"+countryInfo.getPeriods().get(0).getNamePeriod());
        System.out.println("years: "+year);
        System.out.println("corAndValue: "+corAndValue);


              findSuitablePeriods();

             if(!countryPeriods.isEmpty()){

                  findSuitableLiteCoins();

                      if( liteCoins!=null) {
                          log.info(liteCoins.toString());
                          findSuitableCoins();

                         }
             }



    }


    private void findSuitablePeriods() throws IOException {

        countryPeriods=new HashSet<>();

        ArrayList<CountryPeriod> errorPeriods = new ArrayList<>();

       int countryStatus = countryInformation.hashCode();

        countryInformation.getPeriods().forEach((period)->{

            years.forEach(year->{

                if(period.compareData(year)){

                    log.debug("Exist::Checking info about"+countryInformation.getNameCountry()+" period "+period);

                    if (period.getListOnePeriodCountry()==null){

                        try {
                            period.setCurrenciesAndNominalValues();

                            if(period.getCurrenciesAndNominalValues()==null){
                                errorPeriods.add(period);
                           }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SiteConnectionError e) {
                            return;
                        }

                    }

                    countryPeriods.add(period);

                }

            });


        });

        errorPeriods.forEach(countryPeriod -> {

            countryPeriods.remove(countryPeriod);

        });

        if(countryStatus!=countryInformation.hashCode()){

            PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

            SaverInfo saverParseInfo = new SaverInfo(
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

    private void findSuitableCoins() {


         liteCoins.forEach(liteCoin->{

             try {

                 getCoins(liteCoin.getUrl(),liteCoin.getYear(),liteCoin.getValueAndCurrency());
             } catch (SiteConnectionError | IOException e) {
                 log.info("Failed connect to" + liteCoin.getUrl());
                 return;
             }

         });



    }

    public  CoinDto getCoins(String url, Integer year ,String valueAndCurrency) throws IOException, SiteConnectionError {

        PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        Document doc ;
        try {
            doc = UcoinConnection.getUcoinPage(property.open().getProperty("link."+Thread.currentThread().getName())+url);

        } catch (SiteConnectionError e) {
           throw new SiteConnectionError(e.getMessage());
        }
        finally {
            property.close();
        }


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

        if(coinHtml.isEmpty()){
            try{
            String value=valueAndCurrency.split(" ",2)[0];
            String currency=valueAndCurrency.split(" ",2)[1];
            getCoin(url,value,currency,year);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }
            return null;
        }

       coinHtml.forEach(coin->{

            try {
                String value=valueAndCurrency.split(" ",2)[0];
                String currency=valueAndCurrency.split(" ",2)[1];
                getCoin(coin.attr("data-href"),value,currency,year);
           } catch (IOException e) {
               throw new RuntimeException(e);
            } catch (SiteConnectionError e) {
                return;
            }


       });



        return null;


    }


    public  void getCoin(String url, String value, String currency, Integer year) throws IOException, SiteConnectionError {


        CoinDto coinDto = new CoinDto();

        PropertyConnection property = new PropertyConnection(CoinSearcher.PATH_TO_UCOIN_PROPERTY);

        Document doc = null;
        try {
            doc = UcoinConnection.getUcoinPage(property.open().getProperty("link."+Thread.currentThread().getName())+url);
        } catch (SiteConnectionError e) {
            throw new SiteConnectionError(e.getMessage());
        }finally {
            property.close();
        }


        Element infoTableColumns = doc.getElementsByAttributeValue("class","tbl coin-info").first();

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
        coinDto.setValue(value);//new Element("<a href=\"#price\" class=\"gray-12 right pricewj\">Value:&nbsp;<span>none</span></a>")
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
