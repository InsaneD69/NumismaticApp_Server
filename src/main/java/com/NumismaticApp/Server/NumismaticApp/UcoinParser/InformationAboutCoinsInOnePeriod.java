package com.NumismaticApp.Server.NumismaticApp.UcoinParser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class InformationAboutCoinsInOnePeriod   {

    ArrayList<liteCoin> listOnePeriodCountry;

    public ArrayList<liteCoin> getListOnePeriodCountry() {
        return listOnePeriodCountry;
    }

    public  InformationAboutCoinsInOnePeriod(Document periodTablePage){

        listOnePeriodCountry=new ArrayList<>();

        Element table=periodTablePage.select("table").attr("class","year").get(1);// таблица с годами и валютой в html виде

        Elements years= table.getElementsByTag("tr");

        years.forEach((Element year)->{ // для каждой строчки

            year.getElementsByTag("td").forEach((Element value)->{//каждого значения в строчке

                String valueAndCurrency = value.text();

                if(!valueAndCurrency.equals("-")) {

                    listOnePeriodCountry.add(
                            new liteCoin(
                                    year.getElementsByTag("div").text(),
                                    valueAndCurrency,
                                    value.select("a[href]").attr("href")
                            )
                    );

                }

            });



        });

    }






}
