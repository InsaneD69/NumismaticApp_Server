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

        //если на странице с таблицей сселка указаеная не с type=1, то это означает, что таблица на этой странице не с монетами регулярного выпуска
       if(!periodTablePage.getElementsByAttributeValue("class","switcher active").attr("href").contains("type=1"))
       { System.out.println("not exist period, circulation coin is empty"); return;}



        Element table=periodTablePage.select("table").attr("class","year").get(1);// таблица с годами и валютой в html виде

        Elements years= table.getElementsByTag("tr");

       table.getElementsByAttributeValue("class","cell marked-0");




        years.forEach((Element year)->{ // для каждой строчки

            year.getElementsByAttributeValue("class","cell marked-0").forEach((Element value)->{//каждого значения в строчке

                String valueAndCurrency =value.text();// currenciesAndNominalValues.get(value.text());

                if(!valueAndCurrency.equals("-")) {

                    System.out.println(value.text());
                    System.out.println(currenciesAndNominalValues.get(checkUnCorrectTableKey(value.text())));

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
