package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.GsonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CoinSearcher.pathToUcoinProperty;
import static org.junit.jupiter.api.Assertions.*;

class FindCoinTest {


    @Test
    void dasad() throws IOException {

        int year = 2008;
        String url="/coin/russia-2-rubles-2002-2009/?tid=2016";

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+"en")+url).get();
        property.close();
        Element table=doc.select("table").get(3);  //таблица

        Elements  tableCoins = table.getElementsByAttributeValue("class","tr-hr")
                .stream()
                .filter(coin->
                        Integer.compare(Integer.parseInt(coin.getElementsByTag("strong").text()),year)==0
                )
                .collect(Collectors.toCollection(Elements::new));
        ArrayList<String> s = new ArrayList<>();
        tableCoins.forEach(coin->{

          String svfd = coin.attr("data-href");
          s.add(svfd);

          System.out.println(svfd);
        });



        PropertyConnection property1 = new PropertyConnection(pathToUcoinProperty);

        Document docx = Jsoup.connect(property1.open().getProperty("link."+"en")+s.get(0)).get();
        property1.close();
        System.out.println("----------------------------");
        Element ment=docx.select("table").get(0);
        System.out.println(0+"----------------------------");
        System.out.println(ment);
        Element ement=docx.select("table").get(1);
        System.out.println(1+"----------------------------");
        System.out.println(ement);
        Element lement=docx.select("table").get(2);
        System.out.println(2+"----------------------------");
        System.out.println(lement);
        Element tableq=docx.select("table").get(3);
        System.out.println(3+"----------------------------");
        System.out.println(tableq);



    }

}