package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

public class InformationAboutCoinsInOnePeriod   {

    private ArrayList<LiteCoin> listOnePeriodCountry;

    public ArrayList<LiteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }

    public  InformationAboutCoinsInOnePeriod(Document periodTablePage, Map<String,String> currenciesAndNominalValues){

        listOnePeriodCountry=new ArrayList<>();


        Element table=periodTablePage.select("table").attr("class","year").get(1);// таблица с годами и валютой в html виде

        Elements years= table.getElementsByTag("tr");


        years.forEach((Element year)->{ // для каждой строчки

            year.getElementsByAttributeValue("class","cell marked-0").forEach((Element value)->{//каждого значения в строчке

                String valueAndCurrency =value.text();// currenciesAndNominalValues.get(value.text());

                if(!valueAndCurrency.equals("-")) {

                    listOnePeriodCountry.add(
                            new LiteCoin(
                                    Integer.parseInt(year.getElementsByTag("div").text().replace(" ","")),
                                    currenciesAndNominalValues.get(checkUnCorrectTableKey(value.text())),
                                    value.select("a[href]").attr("href")
                            )
                    );

                }

            });



        });



    }





    private String checkUnCorrectTableKey(String text){

       return text.split("/")[0];


    }


}
