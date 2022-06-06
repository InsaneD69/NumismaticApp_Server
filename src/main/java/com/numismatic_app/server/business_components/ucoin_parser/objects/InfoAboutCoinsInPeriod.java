package com.numismatic_app.server.business_components.ucoin_parser.objects;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

public class InfoAboutCoinsInPeriod {

    private ArrayList<LiteCoin> listOnePeriodCountry;

    public ArrayList<LiteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }

    public InfoAboutCoinsInPeriod(Document periodTablePage, Map<String,String> currenciesAndNominalValues){

        listOnePeriodCountry=new ArrayList<>();


        Element table=periodTablePage.select("table").attr("class","year").get(1);// таблица с годами и валютой в html виде

        Elements years= table.getElementsByTag("tr");


        years.forEach(year-> // для каждой строчки

            year.getElementsByAttributeValueContaining("class","cell marked-").forEach( value->{//каждого значения в строчке

                String valueAndCurrency =value.text();

                if(!valueAndCurrency.equals("-")) {

                    listOnePeriodCountry.add(
                            new LiteCoin(
                                    Integer.parseInt(year.getElementsByTag("div").text().replace(" ","")),
                                    currenciesAndNominalValues.get(checkUnCorrectTableKey(value.text())),
                                    value.select("a[href]").attr("href")
                            )
                    );

                }

            })



        );



    }





    private String checkUnCorrectTableKey(String text){

       return text.split("/")[0];


    }


}
